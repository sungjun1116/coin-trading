package sungjun.bitcoin.algorithmtrading.client.binance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sungjun.bitcoin.algorithmtrading.client.IntegrationClientTestSupport;
import sungjun.bitcoin.algorithmtrading.client.binance.response.BinanceAccountApiResponse;

import static org.assertj.core.api.Assertions.assertThat;

class BinanceAccountApiClientTest extends IntegrationClientTestSupport {

    @Autowired
    BinanceAccountApiClient binanceAccountApiClient;

    @DisplayName("전체 자산 잔고를 조회한다.")
    @Test
    void getAccounts() {
        // given
        long timestamp = System.currentTimeMillis();

        // when
        BinanceAccountApiResponse response = binanceAccountApiClient.getAccount(timestamp);

        // then
        assertThat(response).isNotNull();
    }
}
