package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoinoneOrderCancelRequest extends CoinonePrivateBaseRequest {

    private String quoteCurrency;

    private String targetCurrency;

    /**
     * Constructs a CoinoneOrderCancelRequest with the specified access token, nonce, quote currency, and target currency.
     *
     * Instances should be created using the builder pattern.
     *
     * @param accessToken the API access token
     * @param nonce a unique value for request identification
     * @param quoteCurrency the currency to be quoted in the order cancellation
     * @param targetCurrency the currency to be targeted in the order cancellation
     */
    @Builder
    private CoinoneOrderCancelRequest(String accessToken, String nonce, String quoteCurrency, String targetCurrency) {
        super(accessToken, nonce);
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
    }
}
