package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CoinoneAccountRequest extends CoinonePrivateBaseRequest {

    private List<String> currencies;

    @Builder
    private CoinoneAccountRequest(String accessToken, String nonce, List<String> currencies) {
        super(accessToken, nonce);
        this.currencies = currencies;
    }
}
