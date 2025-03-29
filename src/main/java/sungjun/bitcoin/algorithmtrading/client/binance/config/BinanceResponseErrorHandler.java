package sungjun.bitcoin.algorithmtrading.client.binance.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.client.binance.exception.BinanceApiException;

import java.io.IOException;
import java.net.URI;

/**
 * Binance API 응답에 대한 에러 핸들러
 * Binance API는 일반적으로 "code"와 "msg" 필드로 에러 정보를 반환합니다.
 */
@Component
@RequiredArgsConstructor
public class BinanceResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper mapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // HTTP 상태 코드가 4xx, 5xx인 경우 에러로 간주
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            return true;
        }
        
        // 본문 내용을 확인하여 에러 코드가 있는지 확인
        String responseBody = new String(response.getBody().readAllBytes());
        
        // 빈 응답인 경우 에러가 없다고 판단
        if (responseBody.isEmpty()) {
            return false;
        }
        
        try {
            JsonNode jsonNode = mapper.readTree(responseBody);
            // code 필드가 있고 0이 아닌 경우 에러로 간주
            return jsonNode.has("code") && jsonNode.get("code").asInt() != 0;
        } catch (Exception e) {
            // JSON 파싱 실패 시 에러가 없다고 판단 (비정상 응답일 수 있으므로)
            return false;
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String responseBody = new String(response.getBody().readAllBytes());
        
        try {
            JsonNode jsonNode = mapper.readTree(responseBody);
            int errorCode = jsonNode.has("code") ? jsonNode.get("code").asInt() : -1;
            String errorMessage = jsonNode.has("msg") ? jsonNode.get("msg").asText() : "Unknown error";
            
            throw new BinanceApiException(String.valueOf(errorCode), errorMessage);
        } catch (IOException e) {
            // JSON 파싱 실패 시 원본 응답과 HTTP 상태 코드로 예외 발생
            throw new BinanceApiException(String.valueOf(response.getStatusCode().value()), 
                    "Failed to parse error response: " + responseBody);
        }
    }
}
