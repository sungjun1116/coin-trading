package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import sungjun.bitcoin.algorithmtrading.util.SignatureUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RequiredArgsConstructor
public class BinanceSignedClientHttpRequestFactory implements ClientHttpRequestFactory {

    private final BinanceProperties properties;
    private final ClientHttpRequestFactory delegate;

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        if (httpMethod == HttpMethod.GET) {
            // GET 요청: 쿼리 파라미터에 서명 추가
            URI signedUri = addSignatureToUri(uri);
            return delegate.createRequest(signedUri, httpMethod);
        } else {
            // POST 요청: 본문에 서명 추가 로직이 필요한 요청 생성
            return new BinanceSignedClientHttpRequest(delegate.createRequest(uri, httpMethod), properties);
        }
    }

    private URI addSignatureToUri(URI uri) {
        String query = uri.getQuery();
        if (query == null || query.isEmpty()) {
            return uri;
        }

        // HMAC SHA256 서명 생성
        String signature = SignatureUtils.makeSignature(properties.getSecretKey(), query, properties.getSignatureAlgorithm());
        String newQuery = query + "&signature=" + signature;

        return URI.create(uri.getScheme() + "://" + uri.getHost() + uri.getPath() + "?" + newQuery);
    }

    // POST 요청을 위한 래퍼 클래스
    static class BinanceSignedClientHttpRequest implements ClientHttpRequest {
        
        private final ClientHttpRequest delegate;
        private final BinanceProperties properties;
        private final ObjectMapper objectMapper;
        private final ByteArrayOutputStream bodyStream;
        
        public BinanceSignedClientHttpRequest(ClientHttpRequest delegate, BinanceProperties properties) {
            this.delegate = delegate;
            this.properties = properties;
            this.objectMapper = new ObjectMapper();
            this.bodyStream = new ByteArrayOutputStream(1024 * 4); // 4KB 버퍼
        }
        
        @Override
        public OutputStream getBody() {
            // 요청 본문을 캡처하기 위해 ByteArrayOutputStream 반환
            return bodyStream;
        }
        
        @Override
        public HttpMethod getMethod() {
            return delegate.getMethod();
        }
        
        @Override
        public URI getURI() {
            return delegate.getURI();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return Map.of();
        }

        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }
        
        @Override
        public ClientHttpResponse execute() throws IOException {
            byte[] originalBody = bodyStream.toByteArray();
            
            // 1. 원본 요청 본문을 읽어 Map으로 변환
            Map<String, Object> bodyMap = new TreeMap<>();
            if (originalBody.length > 0) {
                String bodyStr = new String(originalBody, StandardCharsets.UTF_8);
                try {
                    bodyMap = objectMapper.readValue(bodyStr, new TypeReference<TreeMap<String, Object>>() {});
                } catch (Exception e) {
                    log.warn("Failed to parse request body as JSON: {}", bodyStr, e);
                    // 파싱 실패 시, 그대로 진행
                }
            }
            
            // 2. 쿼리 스트링 형태로 변환 (정렬된 상태 유지)
            StringBuilder queryBuilder = new StringBuilder();
            bodyMap.forEach((key, value) -> {
                if (!queryBuilder.isEmpty()) {
                    queryBuilder.append("&");
                }
                queryBuilder.append(key).append("=").append(value);
            });
            String queryString = queryBuilder.toString();
            
            // 3. 서명 생성 및 추가
            String signature = SignatureUtils.makeSignature(properties.getSecretKey(), queryString, properties.getSignatureAlgorithm());
            bodyMap.put("signature", signature);
            
            // 4. 서명이 추가된 본문을 JSON으로 변환
            byte[] signedBody = objectMapper.writeValueAsBytes(bodyMap);
            
            // 5. 원래 요청의 본문에 서명이 추가된 본문 작성
            OutputStream delegateOutputStream = delegate.getBody();
            delegateOutputStream.write(signedBody);
            delegateOutputStream.flush();
            
            // 6. 요청 실행
            log.debug("Executing Binance API request with signed body: {}", new String(signedBody, StandardCharsets.UTF_8));

            return delegate.execute();
        }
    }
}
