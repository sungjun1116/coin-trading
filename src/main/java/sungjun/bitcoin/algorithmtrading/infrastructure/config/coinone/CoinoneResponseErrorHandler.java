package sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone;

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
import sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 코인원 API 응답에 대한 에러 핸들러
 * 
 * <p>스트리밍 파싱 방식으로 메모리 효율적으로 처리합니다.
 * 코인원 API는 일반적으로 "result", "error_code", "error_msg" 필드로 에러 정보를 반환합니다.
 * 이 핸들러는 응답을 확인하여 에러 여부를 판단하고, 에러 발생 시 {@link CoinoneApiException}을 발생시킵니다.</p>
 * 
 * <h2>에러 감지 방법</h2>
 * <ul>
 *   <li>HTTP 상태 코드가 4xx 또는 5xx인 경우</li>
 *   <li>응답 본문의 JSON에 "result" 필드가 "error"인 경우</li>
 * </ul>
 * 
 * <h2>에러 응답 예시</h2>
 * <pre>
 * {
 *     "result": "error",
 *     "error_code": "100",
 *     "error_msg": "잘못된 API 키"
 * }
 * </pre>
 * 
 * <h2>에러 처리 예제</h2>
 * <pre>
 * // 에러 핸들러 등록
 * RestClient restClient = RestClient.builder()
 *     .baseUrl(properties.getBaseUrl())
 *     .defaultStatusHandler(coinoneResponseErrorHandler)
 *     .build();
 * 
 * // API 호출 시 자동으로 에러 처리됨
 * try {
 *     restClient.get()
 *         .uri("/v2/account/balance")
 *         .retrieve()
 *         .body(BalanceResponse.class);
 * } catch (CoinoneApiException e) {
 *     // 에러 처리
 *     log.error("코인원 API 에러: [{}] {}", e.getErrorCode(), e.getMessage());
 * }
 * </pre>
 * 
 * <h2>성능 최적화</h2>
 * <p>이 클래스는 메모리 효율성을 위해 Jackson의 스트리밍 파서를 사용합니다.
 * 대용량 JSON 응답을 처리할 때 전체 응답을 메모리에 로드하지 않고 스트리밍 방식으로 필요한 필드만 추출합니다.</p>
 * 
 * <p>더 자세한 에러 코드 정보는 <a href="file:///docs/coinone-error-codes.md">프로젝트 에러 코드 가이드</a>를 참조하세요.</p>
 * 
 * @see CoinoneApiException 에러 응답 시 발생하는 예외 클래스
 * @see <a href="https://docs.coinone.co.kr/">코인원 API 문서</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CoinoneResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper mapper;

    /**
     * 응답에 에러가 있는지 확인합니다.
     * 
     * <p>다음 조건 중 하나라도 해당되면 에러로 판단합니다:</p>
     * <ul>
     *   <li>HTTP 상태 코드가 4xx 또는 5xx인 경우</li>
     *   <li>응답 본문의 JSON에 "result" 필드가 "error"인 경우</li>
     * </ul>
     * 
     * <p>메모리 효율성을 위해 스트리밍 방식으로 JSON을 파싱합니다.</p>
     * 
     * @param response HTTP 응답
     * @return 에러가 있으면 true, 없으면 false
     * @throws IOException 입출력 오류 발생 시
     */
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

    /**
     * 에러 응답을 처리하고 {@link CoinoneApiException}을 발생시킵니다.
     * 
     * <p>응답 본문에서 에러 코드와 메시지를 추출하여 예외 객체를 생성합니다.
     * 메모리 효율성을 위해 스트리밍 방식으로 JSON을 파싱합니다.</p>
     * 
     * <h3>코인원 API 에러 응답 처리 예제</h3>
     * <pre>
     * // 에러가 발생했을 때 처리 방법
     * try {
     *     // 코인원 API 호출
     *     BalanceResponse response = coinoneApiClient.getBalance();
     * } catch (CoinoneApiException e) {
     *     switch (e.getErrorCode()) {
     *         case "100":
     *             log.error("API 키 인증 실패");
     *             // API 키 재발급 로직
     *             break;
     *         case "101":
     *             log.error("API 서명 오류");
     *             // 서명 로직 디버깅
     *             break;
     *         case "502":
     *             log.warn("요청 제한 초과. 잠시 후 재시도합니다.");
     *             // 지수 백오프 방식으로 재시도
     *             Thread.sleep(2000);
     *             // 재시도 로직
     *             break;
     *         default:
     *             log.error("코인원 API 에러: [{}] {}", e.getErrorCode(), e.getErrorMessage());
     *     }
     * }
     * </pre>
     * 
     * @param url 요청 URL
     * @param method HTTP 메소드
     * @param response HTTP 응답
     * @throws CoinoneApiException 코인원 API 에러 발생 시
     * @throws IOException JSON 파싱 실패 시
     */
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
                    String fieldName = parser.currentName();
                    
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
