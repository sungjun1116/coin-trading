package sungjun.bitcoin.algorithmtrading.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.exception.CoinoneApiException;

import java.io.IOException;

public class CoinoneResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        String responseBody = new String(response.getBody().readAllBytes());
        JsonNode jsonNode = mapper.readTree(responseBody);

        return jsonNode.get("result").asText().equals("error");
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String responseBody = new String(response.getBody().readAllBytes());
        JsonNode jsonNode = mapper.readTree(responseBody);
        String errorCode = jsonNode.get("error_code").asText();
        String errorMessage = jsonNode.get("error_msg").asText();

        throw new CoinoneApiException(errorCode, errorMessage);
    }
}
