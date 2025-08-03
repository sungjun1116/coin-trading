package sungjun.bitcoin.algorithmtrading.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.BinanceTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.service.dto.TickerDto;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("TickerService 변동률 계산 테스트")
class TickerServiceChangePercentTest {

    @Mock
    private CoinoneTickerApiClient coinoneTickerApiClient;

    @Mock
    private BinanceTickerApiClient binanceTickerApiClient;

    @InjectMocks
    private TickerService tickerService;

    @Test
    @DisplayName("가격 상승 시 양수 변동률을 계산한다")
    void calculatePositiveChangePercent() {
        // given - 94,000,000원에서 95,000,000원으로 상승 (약 1.0638% 상승)
        setupCoinoneMockWithPrices("94000000", "95000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isPositive();
        // TickerService에서 소수점 4자리까지 반올림하므로 1.0600이 됨
        assertThat(result.getChangePercent()).isEqualByComparingTo(new BigDecimal("1.0600"));
    }

    @Test
    @DisplayName("가격 하락 시 음수 변동률을 계산한다")
    void calculateNegativeChangePercent() {
        // given - 95,000,000원에서 94,000,000원으로 하락 (약 -1.05% 하락)
        setupCoinoneMockWithPrices("95000000", "94000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isNegative();
        // (94000000 - 95000000) / 95000000 * 100 = -1.0526, 소수점 4자리 반올림
        assertThat(result.getChangePercent()).isEqualByComparingTo(new BigDecimal("-1.0500"));
    }

    @Test
    @DisplayName("가격 변동이 없을 때 0% 변동률을 계산한다")
    void calculateZeroChangePercent() {
        // given - 95,000,000원에서 변동 없음
        setupCoinoneMockWithPrices("95000000", "95000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("시작가가 null일 때 0% 변동률을 반환한다")
    void calculateChangePercentWithNullFirstPrice() {
        // given
        setupCoinoneMockWithPrices(null, "95000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("현재가가 null일 때 0% 변동률을 반환한다")
    void calculateChangePercentWithNullLastPrice() {
        // given
        setupCoinoneMockWithPrices("95000000", null);

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("시작가가 0일 때 0% 변동률을 반환한다")
    void calculateChangePercentWithZeroFirstPrice() {
        // given
        setupCoinoneMockWithPrices("0", "95000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("소수점 가격에 대해 정확한 변동률을 계산한다")
    void calculateChangePercentWithDecimalPrices() {
        // given - 100.5에서 105.25로 상승 (약 4.7264% 상승)
        setupCoinoneMockWithPrices("100.5", "105.25");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isPositive();
        // (105.25 - 100.5) / 100.5 * 100 = 4.7264, 소수점 4자리 반올림으로 4.7300
        assertThat(result.getChangePercent()).isEqualByComparingTo(new BigDecimal("4.7300"));
    }

    @Test
    @DisplayName("큰 가격 변동에 대해 정확한 변동률을 계산한다")
    void calculateChangePercentWithLargeChange() {
        // given - 50,000,000원에서 100,000,000원으로 상승 (100% 상승)
        setupCoinoneMockWithPrices("50000000", "100000000");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isEqualByComparingTo(new BigDecimal("100.0000"));
    }

    @Test
    @DisplayName("미세한 가격 변동에 대해 정확한 변동률을 계산한다")
    void calculateChangePercentWithSmallChange() {
        // given - 95,000,000원에서 95,000,001원으로 상승 (0.000001% 상승)
        setupCoinoneMockWithPrices("95000000", "95000001");

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getChangePercent()).isNotNull();
        // 1/95000000 * 100 = 0.0000010526... -> 소수점 4자리 반올림으로 0.0000
        assertThat(result.getChangePercent()).isEqualByComparingTo(new BigDecimal("0.0000"));
    }

    private void setupCoinoneMockWithPrices(String firstPrice, String lastPrice) {
        CoinoneTicker.BestOrderBook bestBid = CoinoneTicker.BestOrderBook.builder()
            .price("94999999")
            .qty("1.0")
            .build();
        
        CoinoneTicker.BestOrderBook bestAsk = CoinoneTicker.BestOrderBook.builder()
            .price("95000001")
            .qty("1.0")
            .build();
        
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("KRW")
            .targetCurrency("BTC")
            .first(firstPrice)
            .last(lastPrice)
            .high("96000000")
            .low("94000000")
            .targetVolume("1234.567")
            .bestBids(List.of(bestBid))
            .bestAsks(List.of(bestAsk))
            .build();

        CoinoneTickerApiResponse response = CoinoneTickerApiResponse.builder()
            .result("success")
            .errorCode(null)
            .serverTime("1700000000000")
            .tickers(List.of(ticker))
            .build();

        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(response);
    }
}
