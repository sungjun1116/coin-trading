package sungjun.bitcoin.algorithmtrading.client.coinone.response;

import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneBalance;

import java.util.List;

@Getter
public class CoinoneAccountApiResponse extends CoinoneBaseApiResponse {

    private List<CoinoneBalance> balances;

    @Builder
    private CoinoneAccountApiResponse(String result, String errorCode, List<CoinoneBalance> balances) {
        super(result, errorCode);
        this.balances = balances;
    }
}
