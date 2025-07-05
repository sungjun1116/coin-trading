package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

/**
 * Coinone API 응답의 공통 기본 클래스입니다.
 * <p>
 * 모든 Coinone API 응답에 공통으로 포함되는 결과 코드와 에러 코드를 정의합니다.
 * JSON 응답의 snake_case 필드명을 camelCase로 매핑합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class CoinoneBaseApiResponse {

    /**
     * API 호출 결과 ("success" 또는 "error")
     */
    protected String result;

    /**
     * 에러 발생 시 에러 코드 (성공 시 null)
     */
    protected String errorCode;

    /**
     * Initializes a CoinoneBaseApiResponse with the specified API result and error code.
     *
     * @param result    the result of the API call, typically "success" or "error"
     * @param errorCode the error code if an error occurred; null if the call was successful
     */
    protected CoinoneBaseApiResponse(String result, String errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }
}
