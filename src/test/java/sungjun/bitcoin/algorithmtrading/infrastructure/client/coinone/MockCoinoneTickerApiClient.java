package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;

import java.util.Collections;
import java.util.List;

public class MockCoinoneTickerApiClient implements CoinoneTickerApiClient {

    @Override
    public CoinoneTickerApiResponse getTicker(String quoteCurrency, String targetCurrency) {
        return CoinoneTickerApiResponse.builder()
            .result("success")
            .tickers(createTicker())
            .build();
    }

    private List<CoinoneTicker> createTicker() {
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("krw")
            .targetCurrency("btc")
            .build();

        return Collections.singletonList(ticker);
    }

}
