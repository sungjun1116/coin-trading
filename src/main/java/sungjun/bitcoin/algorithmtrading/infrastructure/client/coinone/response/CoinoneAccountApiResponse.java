package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response;

import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneBalance;

import java.util.List;

/**
 * Coinone 계정 조회 API의 응답 데이터를 나타내는 클래스입니다.
 * <p>
 * 사용자의 계정 잔고 정보 목록을 포함합니다.
 * 각 통화별로 사용 가능한 잔고, 제한 금액, 평균 매수가 등을 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
public class CoinoneAccountApiResponse extends CoinoneBaseApiResponse {

    /**
     * 계정 잔고 정보 목록
     */
    private List<CoinoneBalance> balances;

    /**
     * CoinoneAccountApiResponse 생성자입니다.
     *
     * @param result API 호출 결과
     * @param errorCode 에러 코드
     * @param balances 계정 잔고 정보 목록
     */
    @Builder
    private CoinoneAccountApiResponse(String result, String errorCode, List<CoinoneBalance> balances) {
        super(result, errorCode);
        this.balances = balances;
    }
}
