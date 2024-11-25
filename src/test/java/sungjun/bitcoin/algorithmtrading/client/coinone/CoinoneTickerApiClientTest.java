package sungjun.bitcoin.algorithmtrading.client.coinone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sungjun.bitcoin.algorithmtrading.client.coinone.response.CoinoneTickerApiResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CoinoneTickerApiClientTest {

    @Autowired
    CoinoneTickerApiClient coinoneTickerApiClient;

    @DisplayName("개별 ticker 정보를 조회한다.")
    @Test
    void getTicker() {
        // given
        String quoteCurrency = "KRW";
        String targetCurrency = "BTC";

        // when
        CoinoneTickerApiResponse ticker = coinoneTickerApiClient.getTicker(quoteCurrency, targetCurrency);

        // then
        assertThat(ticker.getResult()).isEqualTo("success");
    }

}
