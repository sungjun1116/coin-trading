package sungjun.bitcoin.algorithmtrading.api.service;

import org.springframework.stereotype.Service;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTiker;

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
