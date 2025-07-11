package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.IntegrationClientTestSupport;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneOrderApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.OrderSide;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneOrderCancelRequest;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneOrderRequest;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneOrderApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CoinoneOrderApiClientTest extends IntegrationClientTestSupport {

    @Autowired
    CoinoneOrderApiClient coinoneOrderApiClient;

    @Autowired
    sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone.CoinoneProperties coinoneProperties;

    @AfterEach
    void tearDown() {
        // coinoneOrderApiClient.cancelAll(createOrderCancelRequest());
    }

    @DisplayName("시장가 매도 주문을 체결한다.")
    @Disabled
    @Test
    void marketSellOrder() {
        // given
        String accessToken = coinoneProperties.getAccessToken();
        String nonce = UUID.randomUUID().toString();
        OrderSide side = OrderSide.SELL;
        String quoteCurrency = "KRW";
        String targetCurrency = "BTC";
        String qty = new BigDecimal("0.00005").toString(); // BigDecimal로 생성


        CoinoneOrderRequest limitOrderRequest = CoinoneOrderRequest.createMarketSellOrder(accessToken,
            nonce,
            side,
            quoteCurrency,
            targetCurrency,
            qty,
            null);

        // when
        CoinoneOrderApiResponse response = coinoneOrderApiClient.order(limitOrderRequest);

        // then
        assertThat(response).isNotNull();
    }


    @DisplayName("지정가 매도 주문을 체결한다.")
    // @Disabled
    @Test
    void limitSellOrder() {
        // given
        String accessToken = coinoneProperties.getAccessToken();
        String nonce = UUID.randomUUID().toString();
        OrderSide side = OrderSide.SELL;
        String quoteCurrency = "KRW";
        String targetCurrency = "BTC";
        String price = String.valueOf(146530000);
        String qty = new BigDecimal("0.01055689").toString();
        CoinoneOrderRequest limitOrderRequest = CoinoneOrderRequest.createLimitOrder(accessToken,
            nonce,
            side,
            quoteCurrency,
            targetCurrency,
            price,
            qty,
            true);

        // when
        CoinoneOrderApiResponse response = coinoneOrderApiClient.order(limitOrderRequest);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("시장가 매수 주문을 체결한다.")
    @Disabled
    @Test
    void marketBuyOrder() {
        // given
        String accessToken = coinoneProperties.getAccessToken();
        String nonce = UUID.randomUUID().toString();
        OrderSide side = OrderSide.BUY;
        String quoteCurrency = "KRW";
        String targetCurrency = "TRX";
        String amount = "1996002";

        CoinoneOrderRequest limitOrderRequest = CoinoneOrderRequest.createMarketBuyOrder(accessToken,
            nonce,
            side,
            quoteCurrency,
            targetCurrency,
            amount,
            null);

        // when
        CoinoneOrderApiResponse response = coinoneOrderApiClient.order(limitOrderRequest);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("지정가 매수 주문을 체결한다.")
    @Disabled
    @Test
    void limitBuyOrder() {
        // given
        String accessToken = coinoneProperties.getAccessToken();
        String nonce = UUID.randomUUID().toString();
        OrderSide side = OrderSide.BUY;
        String quoteCurrency = "KRW";
        String targetCurrency = "TRX";
        String price = "349.6";
        String qty = "2864.98194143";

        CoinoneOrderRequest limitOrderRequest = CoinoneOrderRequest.createLimitOrder(accessToken,
            nonce,
            side,
            quoteCurrency,
            targetCurrency,
            price,
            qty,
            true);

        // when
        CoinoneOrderApiResponse response = coinoneOrderApiClient.order(limitOrderRequest);

        // then
        assertThat(response).isNotNull();
    }

    private CoinoneOrderCancelRequest createOrderCancelRequest() {
        return CoinoneOrderCancelRequest.builder()
            .accessToken(coinoneProperties.getAccessToken())
            .nonce(UUID.randomUUID().toString())
            .quoteCurrency("KRW")
            .targetCurrency("BTC")
            .build();
    }

}
