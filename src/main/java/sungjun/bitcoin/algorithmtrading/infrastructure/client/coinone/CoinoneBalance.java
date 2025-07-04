package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * Coinone 거래소의 계정 잔고 정보를 나타내는 데이터 클래스입니다.
 * <p>
 * 특정 통화에 대한 사용 가능한 잔고, 제한 금액, 평균 매수가 등의 정보를 포함합니다.
 * JSON 응답에서 snake_case 형태의 필드명을 camelCase로 매핑합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CoinoneBalance {

    /**
     * 사용 가능한 잔고 금액 (문자열 형태)
     */
    private String available;

    /**
     * 제한 금액 또는 총 잔고 (문자열 형태)
     */
    private String limit;

    /**
     * 평균 매수가 (문자열 형태)
     */
    private String averagePrice;

    /**
     * 통화 코드 (KRW, BTC 등)
     */
    private String currency;

    /**
     * CoinoneBalance 생성자입니다.
     *
     * @param available 사용 가능한 잔고
     * @param limit 제한 금액
     * @param averagePrice 평균 매수가
     * @param currency 통화 코드
     */
    @Builder
    private CoinoneBalance(String available, String limit, String averagePrice, String currency) {
        this.available = available;
        this.limit = limit;
        this.averagePrice = averagePrice;
        this.currency = currency;
    }
}
