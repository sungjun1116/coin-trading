package sungjun.bitcoin.algorithmtrading.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.service.CoinoneService;

import static org.assertj.core.api.Assertions.assertThat;

class CoinoneServiceTest extends IntegrationServiceTestSupport {

    @Autowired
    CoinoneService coinoneService;

    @DisplayName("개별 ticket 정보를 조회한다.")
    @Test
    void getTicker() {
        // given
        String quoteCurrency = "krw";
        String targetCurrency = "btc";

        // when
        CoinoneTicker ticker = coinoneService.getTicker(quoteCurrency, targetCurrency);

        // then
        assertThat(ticker.getQuoteCurrency()).isEqualTo(quoteCurrency);
        assertThat(ticker.getTargetCurrency()).isEqualTo(targetCurrency);
    }

}
