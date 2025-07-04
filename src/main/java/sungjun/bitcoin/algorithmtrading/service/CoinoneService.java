package sungjun.bitcoin.algorithmtrading.service;

import org.springframework.stereotype.Service;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTiker;

@Service
public class CoinoneService {

    private final CoinoneTickerApiClient coinoneTickerApiClient;

    public CoinoneService(CoinoneTickerApiClient coinoneTickerApiClient) {
        this.coinoneTickerApiClient = coinoneTickerApiClient;
    }

    public CoinoneTiker getTicker(String quoteCurrency, String targetCurrency) {
        CoinoneTickerApiResponse apiResponse = coinoneTickerApiClient.getTicker(quoteCurrency, targetCurrency);
        return apiResponse.getTickers().getFirst();
    }

}
