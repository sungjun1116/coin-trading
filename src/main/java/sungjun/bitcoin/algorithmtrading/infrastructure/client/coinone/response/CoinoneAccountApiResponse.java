package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response;

import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneBalance;

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
