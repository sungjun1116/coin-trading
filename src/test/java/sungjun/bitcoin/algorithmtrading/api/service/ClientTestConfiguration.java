package sungjun.bitcoin.algorithmtrading.api.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sungjun.bitcoin.algorithmtrading.client.MockCoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTickerApiClient;

@TestConfiguration
public class ClientTestConfiguration {

    @Bean
    CoinoneService coinoneService() {
        return new CoinoneService(mockCoinoneTickerApiClient());
    }

    @Bean
    CoinoneTickerApiClient mockCoinoneTickerApiClient() {
        return new MockCoinoneTickerApiClient();
    }
}
