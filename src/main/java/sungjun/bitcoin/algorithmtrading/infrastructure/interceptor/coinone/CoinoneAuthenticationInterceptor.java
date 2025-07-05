package sungjun.bitcoin.algorithmtrading.infrastructure.interceptor.coinone;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone.CoinoneProperties;
import sungjun.bitcoin.algorithmtrading.util.SignatureUtils;

import java.io.IOException;
import java.util.Base64;

import static sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone.CoinoneProperties.X_COINONE_PAYLOAD;
import static sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone.CoinoneProperties.X_COINONE_SIGNATURE;

/**
 * Coinone API 인증을 처리하는 HTTP 요청 인터셉터입니다.
 * <p>
 * 비공개 API 호출 시 필요한 인증 헤더를 자동으로 추가합니다.
 * 요청 본문을 Base64로 인코딩하고 HMAC 서명을 생성하여
 * X-COINONE-PAYLOAD와 X-COINONE-SIGNATURE 헤더를 설정합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class CoinoneAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final CoinoneProperties properties;

    /**
     * Adds Coinone API authentication headers to an outgoing HTTP request.
     *
     * Encodes the request body in Base64 and generates an HMAC signature using the configured secret key and algorithm.
     * Sets the `X-COINONE-PAYLOAD` and `X-COINONE-SIGNATURE` headers before forwarding the request for execution.
     *
     * @param request the HTTP request being intercepted
     * @param body the request body as a byte array
     * @param execution the context for executing the request
     * @return the HTTP response resulting from the executed request
     * @throws IOException if an I/O error occurs during request execution
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String encodedPayload = Base64.getEncoder().encodeToString(body);
        String signature = SignatureUtils.makeSignature(properties.getSecretKey(), encodedPayload, properties.getSignatureAlgorithm());
        request.getHeaders().add(X_COINONE_PAYLOAD, encodedPayload);
        request.getHeaders().add(X_COINONE_SIGNATURE, signature);

        return execution.execute(request, body);
    }
}
