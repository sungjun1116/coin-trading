package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response;

import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;

import java.util.List;

@Getter
public class CoinoneTickerApiResponse extends CoinoneBaseApiResponse {

    private String serverTime;

    private List<CoinoneTicker> tickers;

    @Builder
    private CoinoneTickerApiResponse(String result, String errorCode, String serverTime, List<CoinoneTicker> tickers) {
        super(result, errorCode);
        this.serverTime = serverTime;
        this.tickers = tickers;
    }
}
