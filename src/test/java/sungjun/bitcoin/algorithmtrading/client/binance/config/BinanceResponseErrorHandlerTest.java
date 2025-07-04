package sungjun.bitcoin.algorithmtrading.client.binance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.config.BinanceResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.exception.BinanceApiException;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinanceResponseErrorHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BinanceResponseErrorHandler errorHandler = new BinanceResponseErrorHandler(objectMapper);

    @Test
    @DisplayName("HTTP 상태 코드가 4xx인 경우 에러로 판단해야 함")
    void hasError_shouldReturnTrue_whenHttpStatusIs4xx() throws IOException {
        // given
        ClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.BAD_REQUEST);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertTrue(hasError);
    }

    @Test
    @DisplayName("HTTP 상태 코드가 5xx인 경우 에러로 판단해야 함")
    void hasError_shouldReturnTrue_whenHttpStatusIs5xx() throws IOException {
        // given
        ClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertTrue(hasError);
    }

    @Test
    @DisplayName("응답에 0이 아닌 에러 코드가 있는 경우 에러로 판단해야 함")
    void hasError_shouldReturnTrue_whenResponseContainsErrorCode() throws IOException {
        // given
        String errorResponse = "{\"code\":-1121,\"msg\":\"Invalid symbol.\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                errorResponse.getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertTrue(hasError);
    }

    @Test
    @DisplayName("응답의 코드가 0인 경우 정상으로 판단해야 함")
    void hasError_shouldReturnFalse_whenResponseHasCodeZero() throws IOException {
        // given
        String okResponse = "{\"code\":0,\"msg\":\"Success\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                okResponse.getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertFalse(hasError);
    }

    @Test
    @DisplayName("응답이 비어있는 경우 정상으로 판단해야 함")
    void hasError_shouldReturnFalse_whenResponseIsEmpty() throws IOException {
        // given
        ClientHttpResponse response = new MockClientHttpResponse(
                "".getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertFalse(hasError);
    }

    @Test
    @DisplayName("JSON 파싱에 실패한 경우 정상으로 판단해야 함")
    void hasError_shouldReturnFalse_whenJsonParsingFails() throws IOException {
        // given
        String invalidJson = "{invalid json}";
        ClientHttpResponse response = new MockClientHttpResponse(
                invalidJson.getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertFalse(hasError);
    }

    @Test
    @DisplayName("에러 응답을 올바르게 BinanceApiException으로 변환해야 함")
    void handleError_shouldThrowBinanceApiException_withCorrectErrorInfo() {
        // given
        String errorResponse = "{\"code\":-1121,\"msg\":\"Invalid symbol.\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                errorResponse.getBytes(), HttpStatus.OK);
        URI uri = URI.create("https://api.binance.com/api/v3/ticker/price");

        // when & then
        BinanceApiException exception = assertThrows(BinanceApiException.class, () ->
                errorHandler.handleError(uri, HttpMethod.GET, response));

        assertEquals(-1121, exception.getErrorCode());
        assertEquals("Invalid symbol.", exception.getErrorMessage());
        assertEquals("Binance API Error: [-1121] Invalid symbol.", exception.getMessage());
    }

    @Test
    @DisplayName("JSON 파싱 에러 발생 시 적절한 예외를 발생시켜야 함")
    void handleError_shouldHandleParsingErrors() {
        // given
        String invalidJson = "{invalid json}";
        ClientHttpResponse response = new MockClientHttpResponse(
                invalidJson.getBytes(), HttpStatus.BAD_REQUEST);
        URI uri = URI.create("https://api.binance.com/api/v3/ticker/price");

        // when & then
        BinanceApiException exception = assertThrows(BinanceApiException.class, () ->
                errorHandler.handleError(uri, HttpMethod.GET, response));

        assertEquals(400, exception.getErrorCode());
        assertTrue(exception.getErrorMessage().contains("Failed to parse error response"));
    }
}
