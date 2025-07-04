package sungjun.bitcoin.algorithmtrading.client.coinone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.config.CoinoneResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.exception.CoinoneApiException;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoinoneResponseErrorHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CoinoneResponseErrorHandler errorHandler = new CoinoneResponseErrorHandler(objectMapper);

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
    @DisplayName("result 필드가 error인 경우 에러로 판단해야 함")
    void hasError_shouldReturnTrue_whenResultIsError() throws IOException {
        // given
        String errorResponse = "{\"result\":\"error\",\"error_code\":\"100\",\"error_msg\":\"잘못된 API 키\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                errorResponse.getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertTrue(hasError);
    }

    @Test
    @DisplayName("result 필드가 success인 경우 정상으로 판단해야 함")
    void hasError_shouldReturnFalse_whenResultIsSuccess() throws IOException {
        // given
        String okResponse = "{\"result\":\"success\",\"balance\":\"10000\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                okResponse.getBytes(), HttpStatus.OK);

        // when
        boolean hasError = errorHandler.hasError(response);

        // then
        assertFalse(hasError);
    }

    @Test
    @DisplayName("result 필드가 없는 경우 정상으로 판단해야 함")
    void hasError_shouldReturnFalse_whenNoResultField() throws IOException {
        // given
        String okResponse = "{\"data\":\"some data\"}";
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
    @DisplayName("에러 응답을 올바르게 CoinoneApiException으로 변환해야 함")
    void handleError_shouldThrowCoinoneApiException_withCorrectErrorInfo() {
        // given
        String errorResponse = "{\"result\":\"error\",\"error_code\":\"100\",\"error_msg\":\"잘못된 API 키\"}";
        ClientHttpResponse response = new MockClientHttpResponse(
                errorResponse.getBytes(), HttpStatus.OK);
        URI uri = URI.create("https://api.coinone.co.kr/v2/account/balance");

        // when & then
        CoinoneApiException exception = assertThrows(CoinoneApiException.class, () ->
                errorHandler.handleError(uri, HttpMethod.GET, response));

        assertEquals("100", exception.getErrorCode());
        assertEquals("잘못된 API 키", exception.getMessage());
    }

    @Test
    @DisplayName("JSON 파싱 에러 발생 시 적절한 예외를 발생시켜야 함")
    void handleError_shouldHandleParsingErrors() {
        // given
        String invalidJson = "{invalid json}";
        ClientHttpResponse response = new MockClientHttpResponse(
                invalidJson.getBytes(), HttpStatus.BAD_REQUEST);
        URI uri = URI.create("https://api.coinone.co.kr/v2/account/balance");

        // when & then
        Exception exception = assertThrows(Exception.class, () ->
                errorHandler.handleError(uri, HttpMethod.GET, response));

        assertNotNull(exception);
    }
}
