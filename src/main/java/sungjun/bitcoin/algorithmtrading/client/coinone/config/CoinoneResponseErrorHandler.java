package sungjun.bitcoin.algorithmtrading.client.coinone.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import sungjun.bitcoin.algorithmtrading.client.coinone.exception.CoinoneApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 코인원 API 응답에 대한 에러 핸들러
 * 스트리밍 파싱 방식으로 메모리 효율적으로 처리합니다.
 * 코인원 API는 일반적으로 "result", "error_code", "error_msg" 필드로 에러 정보를 반환합니다.
 */
@Slf4j
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

        // 스트리밍 방식으로 응답 본문을 확인하여 에러 코드가 있는지 확인
        InputStream inputStream = response.getBody();
        
        // 입력 스트림이 비어있는 경우
        if (inputStream.available() <= 0) {
            return false;
        }
        
        try {
            // Jackson의 스트리밍 파서 사용
            JsonFactory factory = mapper.getFactory();
            try (JsonParser parser = factory.createParser(inputStream)) {
                // 루트 객체를 시작하는 토큰 확인
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    return false; // JSON 객체가 아닌 경우
                }
                
                // 필드를 하나씩 확인하며 "result" 필드 검색
                while (parser.nextToken() != JsonToken.END_OBJECT && parser.currentToken() != null) {
                    String fieldName = parser.currentName();
                    if ("result".equals(fieldName)) {
                        // "result" 필드를 찾았으면 값으로 이동
                        parser.nextToken();
                        // 코인원은 "result"가 "error"인 경우 오류로 간주
                        String result = parser.getText();
                        return "error".equals(result);
                    }
                    // 다른 필드의 값은 건너뛰기
                    if (parser.currentToken() != JsonToken.FIELD_NAME) {
                        parser.skipChildren();
                    }
                }
                return false; // "result" 필드를 찾지 못함
            }
        } catch (Exception e) {
            log.debug("JSON 파싱 중 오류 발생: {}", e.getMessage());
            return false; // JSON 파싱 실패 시 에러가 없다고 판단
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        int statusCode = response.getStatusCode().value();
        InputStream inputStream = response.getBody();
        
        try {
            // Jackson의 스트리밍 파서 사용
            JsonFactory factory = mapper.getFactory();
            String errorCode = String.valueOf(statusCode); // 기본값으로 HTTP 상태 코드 사용
            String errorMessage = "Unknown error";
            
            try (JsonParser parser = factory.createParser(inputStream)) {
                // 루트 객체 시작 토큰 확인
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("Invalid JSON format: not an object");
                }
                
                // JSON 필드를 반복하며 error_code와 error_msg 필드 찾기
                while (parser.nextToken() != JsonToken.END_OBJECT && parser.currentToken() != null) {
                    String fieldName = parser.getCurrentName();
                    
                    if ("error_code".equals(fieldName)) {
                        parser.nextToken();
                        errorCode = parser.getText();
                    } else if ("error_msg".equals(fieldName)) {
                        parser.nextToken();
                        errorMessage = parser.getText();
                    } else {
                        // 관심 없는 필드는 건너뛰기
                        parser.nextToken();
                        parser.skipChildren();
                    }
                }
            }
            
            log.debug("Coinone API Error: [{}] {} (URL: {})", errorCode, errorMessage, url);
            throw new CoinoneApiException(errorCode, errorMessage);
        } catch (IOException e) {
            // 스트리밍 파싱 실패 시 상태 코드를 기반으로 예외 발생
            log.warn("Failed to parse Coinone error response: {}", e.getMessage());
            throw new CoinoneApiException(String.valueOf(statusCode), "Failed to parse error response: " + e.getMessage());
        }
    }
}
