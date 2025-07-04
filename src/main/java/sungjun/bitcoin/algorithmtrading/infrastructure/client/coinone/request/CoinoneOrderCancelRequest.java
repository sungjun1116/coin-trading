package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoinoneOrderCancelRequest extends CoinonePrivateBaseRequest {

    private String quoteCurrency;

    private String targetCurrency;

    @Builder
    private CoinoneOrderCancelRequest(String accessToken, String nonce, String quoteCurrency, String targetCurrency) {
        super(accessToken, nonce);
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
    }
}
