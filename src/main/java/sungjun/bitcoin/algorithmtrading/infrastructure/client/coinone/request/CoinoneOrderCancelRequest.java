package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoinoneOrderCancelRequest extends CoinonePrivateBaseRequest {

    private String quoteCurrency;

    private String targetCurrency;

    /****
     * Creates a request object for canceling an order on the Coinone platform with the specified access token, nonce, quote currency, and target currency.
     *
     * This constructor is intended for use by the builder pattern.
     *
     * @param accessToken the API access token
     * @param nonce a unique value for request identification
     * @param quoteCurrency the quoted currency in the cancellation request
     * @param targetCurrency the target currency in the cancellation request
     */
    @Builder
    private CoinoneOrderCancelRequest(String accessToken, String nonce, String quoteCurrency, String targetCurrency) {
        super(accessToken, nonce);
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
    }
}
