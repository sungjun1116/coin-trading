package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Coinone 계정 조회 API 요청 데이터 클래스입니다.
 * <p>
 * 사용자의 계정 잔고 정보를 조회하기 위한 요청 파라미터를 정의합니다.
 * 특정 통화 목록을 지정하여 해당 통화들의 잔고만 조회할 수 있습니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
public class CoinoneAccountRequest extends CoinonePrivateBaseRequest {

    /**
     * 조회할 통화 코드 목록 (예: ["KRW", "BTC", "ETH"])
     * null인 경우 모든 통화 조회
     */
    private List<String> currencies;

    /**
     * CoinoneAccountRequest 생성자입니다.
     *
     * @param accessToken 액세스 토큰
     * @param nonce 요청 고유 번호
     * @param currencies 조회할 통화 목록
     */
    @Builder
    private CoinoneAccountRequest(String accessToken, String nonce, List<String> currencies) {
        super(accessToken, nonce);
        this.currencies = currencies;
    }
}
