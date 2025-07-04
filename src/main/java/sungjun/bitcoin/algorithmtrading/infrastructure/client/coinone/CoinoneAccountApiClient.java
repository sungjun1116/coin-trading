package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneAccountRequest;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneAccountApiResponse;

/**
 * Coinone 거래소의 계정 정보 조회 API 클라이언트입니다.
 * <p>
 * 이 클라이언트는 Coinone API의 account 엔드포인트와 통신하여
 * 사용자의 계정 잔고 및 자산 정보를 제공합니다.
 * 모든 API 호출은 인증이 필요합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@HttpExchange
public interface CoinoneAccountApiClient {

    /**
     * 특정 통화의 계정 잔고를 조회합니다.
     * <p>
     * 지정된 통화에 대한 사용자의 계정 잔고 정보를 가져옵니다.
     * </p>
     *
     * @param requestBody 계정 조회 요청 데이터
     * @return {@link CoinoneAccountApiResponse} 계정 정보 응답
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException API 호출 실패 시
     */
    @PostExchange("/account/balance")
    CoinoneAccountApiResponse getAccount(@RequestBody CoinoneAccountRequest requestBody);

    /**
     * 모든 통화의 계정 잔고를 일괄 조회합니다.
     * <p>
     * 사용자 계정에 있는 모든 통화(코인)의 잔고 정보를 가져옵니다.
     * </p>
     *
     * @param requestBody 계정 조회 요청 데이터
     * @return {@link CoinoneAccountApiResponse} 전체 계정 정보 응답
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException API 호출 실패 시
     */
    @PostExchange("/account/balance/all")
    CoinoneAccountApiResponse getAccounts(@RequestBody CoinoneAccountRequest requestBody);

}
