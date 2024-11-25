package sungjun.bitcoin.algorithmtrading.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTiker;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ClientTestConfiguration.class)
@SpringBootTest
class CoinoneServiceTest {

    @Autowired
    CoinoneService coinoneService;

    @DisplayName("개별 ticket 정보를 조회한다.")
    @Test
    void getTicker() {
        // given
        String quoteCurrency = "KRW";
        String targetCurrency = "BTC";

        // when
        CoinoneTiker ticker = coinoneService.getTicker(quoteCurrency, targetCurrency);

        // then
        assertThat(ticker.getQuoteCurrency()).isEqualTo(quoteCurrency);
        assertThat(ticker.getTargetCurrency()).isEqualTo(targetCurrency);
    }

}
