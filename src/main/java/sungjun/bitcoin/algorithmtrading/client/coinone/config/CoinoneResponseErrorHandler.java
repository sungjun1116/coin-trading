package sungjun.bitcoin.algorithmtrading.client.coinone.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.client.binance.exception.BinanceApiException;
import sungjun.bitcoin.algorithmtrading.client.coinone.exception.CoinoneApiException;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class CoinoneResponseErrorHandler implements ResponseErrorHandler {

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
            // "result" 필드가 "error"인 경우 에러로 간주
            return jsonNode.has("result") && jsonNode.get("result").asText().equals("error");
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
            String errorCode = jsonNode.has("error_code") ? jsonNode.get("error_code").asText() : "Unknown error code";
            String errorMessage = jsonNode.has("error_msg") ? jsonNode.get("error_msg").asText() : "Unknown error message";

            throw new CoinoneApiException(errorCode, errorMessage);
        } catch (IOException e) {
            // JSON 파싱 실패 시 에러가 없다고 판단 (비정상 응답일 수 있으므로)
            throw new BinanceApiException(String.valueOf(response.getStatusCode().value()),
                "Failed to parse error response: " + responseBody);
        }
    }
}
