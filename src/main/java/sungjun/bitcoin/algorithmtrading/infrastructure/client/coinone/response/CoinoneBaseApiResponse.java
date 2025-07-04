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
     * CoinoneBaseApiResponse 생성자입니다.
     *
     * @param result API 호출 결과
     * @param errorCode 에러 코드
     */
    protected CoinoneBaseApiResponse(String result, String errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }
}
