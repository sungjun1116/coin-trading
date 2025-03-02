package sungjun.bitcoin.algorithmtrading.client.coinone.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CoinoneOrderApiResponse extends CoinoneBaseApiResponse {

    private String orderId;

    @Builder
    private CoinoneOrderApiResponse(String result, String errorCode, String orderId) {
        super(result, errorCode);
        this.orderId = orderId;
    }
}
