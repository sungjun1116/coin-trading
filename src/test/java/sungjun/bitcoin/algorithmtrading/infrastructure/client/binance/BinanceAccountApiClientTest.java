package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceAccountApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceBalance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BinanceAccountApiClientTest {

    @DisplayName("전체 자산 잔고를 조회한다.")
    @Test
    void getAccounts() {
        // given
        BinanceAccountApiClient binanceAccountApiClient = mock(BinanceAccountApiClient.class);
        BinanceBalance btcBalance = BinanceBalance.builder()
            .asset("BTC")
            .free("0.5")
            .locked("0.0")
            .build();
        BinanceBalance usdtBalance = BinanceBalance.builder()
            .asset("USDT")
            .free("1000.0")
            .locked("0.0")
            .build();

        BinanceAccountApiResponse mockResponse = BinanceAccountApiResponse.builder()
            .makerCommission(0)
            .takerCommission(0)
            .buyerCommission(0)
            .sellerCommission(0)
            .canTrade(true)
            .canWithdraw(true)
            .canDeposit(true)
            .commissionRates(null)
            .brokered(false)
            .requireSelfTradePrevention(false)
            .preventSor(false)
            .updateTime(System.currentTimeMillis())
            .accountType("SPOT")
            .balances(List.of(btcBalance, usdtBalance))
            .permissions(null)
            .build();

        given(binanceAccountApiClient.getAccount(anyBoolean(), anyLong(), anyLong()))
            .willReturn(mockResponse);

        // when
        BinanceAccountApiResponse response = binanceAccountApiClient.getAccount(false, 5000L, System.currentTimeMillis());

        // then
        verify(binanceAccountApiClient).getAccount(anyBoolean(), anyLong(), anyLong());
        assertThat(response).isNotNull();
        assertThat(response.getBalances()).hasSize(2);
        assertThat(response.getBalances().get(0).getAsset()).isEqualTo("BTC");
        assertThat(response.getBalances().get(1).getAsset()).isEqualTo("USDT");
    }
}
