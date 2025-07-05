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
     * Retrieves the account balance for a specific currency from the Coinone exchange.
     *
     * @param requestBody the request data specifying the currency to query
     * @return the account balance information for the specified currency
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    @PostExchange("/account/balance")
    CoinoneAccountApiResponse getAccount(@RequestBody CoinoneAccountRequest requestBody);

    /**
     * Retrieves account balances for all currencies held by the user.
     *
     * @param requestBody the account request data specifying authentication and query details
     * @return a response containing the complete account balance information across all currencies
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    @PostExchange("/account/balance/all")
    CoinoneAccountApiResponse getAccounts(@RequestBody CoinoneAccountRequest requestBody);

}
