package sungjun.bitcoin.algorithmtrading.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoinoneOrderCancleRequest extends CoinonePrivateBaseRequest {

    private String quoteCurrency;

    private String targetCurrency;

    @Builder
    private CoinoneOrderCancleRequest(String accessToken, String nonce, String quoteCurrency, String targetCurrency) {
        super(accessToken, nonce);
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
    }
}
