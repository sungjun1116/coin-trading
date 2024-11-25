package sungjun.bitcoin.algorithmtrading.client.coinone.response;

import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTiker;

import java.util.List;

@Getter
public class CoinoneTickerApiResponse extends CoinoneBaseApiResponse {

    private String serverTime;

    private List<CoinoneTiker> tickers;

    @Builder
    private CoinoneTickerApiResponse(String result, String errorCode, String serverTime, List<CoinoneTiker> tickers) {
        super(result, errorCode);
        this.serverTime = serverTime;
        this.tickers = tickers;
    }
}
