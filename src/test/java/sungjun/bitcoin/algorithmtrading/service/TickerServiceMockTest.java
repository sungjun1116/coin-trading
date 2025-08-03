package sungjun.bitcoin.algorithmtrading.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.BinanceTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.service.dto.TickerDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TickerService Mock 의존성 테스트")
class TickerServiceMockTest {

    @Mock
    private CoinoneTickerApiClient coinoneTickerApiClient;

    @Mock
    private BinanceTickerApiClient binanceTickerApiClient;

    @InjectMocks
    private TickerService tickerService;

    @Test
    @DisplayName("getPopularTickers 호출 시 올바른 파라미터로 API를 호출한다")
    void should_CallApisWithCorrectParameters_When_GetPopularTickers() {
        // given
        setupDefaultMocks();

        // when
        tickerService.getPopularTickers();

        // then
        ArgumentCaptor<String> coinoneQuoteCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> coinoneTargetCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> binanceSymbolCaptor = ArgumentCaptor.forClass(String.class);

        verify(coinoneTickerApiClient, times(2))
            .getTicker(coinoneQuoteCaptor.capture(), coinoneTargetCaptor.capture());
        verify(binanceTickerApiClient, times(2))
            .getTicker(binanceSymbolCaptor.capture());

        // Coinone API 호출 검증
        List<String> coinoneQuoteValues = coinoneQuoteCaptor.getAllValues();
        List<String> coinoneTargetValues = coinoneTargetCaptor.getAllValues();
        assertThat(coinoneQuoteValues).containsExactly("KRW", "KRW");
        assertThat(coinoneTargetValues).containsExactly("BTC", "ETH");

        // Binance API 호출 검증
        List<String> binanceSymbolValues = binanceSymbolCaptor.getAllValues();
        assertThat(binanceSymbolValues).containsExactly("BTCUSDT", "ETHUSDT");
    }

    @Test
    @DisplayName("getAllTickers 호출 시 병렬로 API를 호출한다")
    void should_CallApisInParallel_When_GetAllTickers() {
        // given
        setupCoinoneMock("BTC");
        setupBinanceMock("BTC");

        // when
        List<TickerDto> tickers = tickerService.getAllTickers("BTC");

        // then
        verify(coinoneTickerApiClient, times(1)).getTicker("KRW", "BTC");
        verify(binanceTickerApiClient, times(1)).getTicker("BTCUSDT");
        assertThat(tickers).hasSize(2);
    }

    @Test
    @DisplayName("getCoinoneTicker 호출 시 올바른 파라미터로 Coinone API를 호출한다")
    void should_CallCoinoneApiWithCorrectParameters_When_GetCoinoneTicker() {
        // given
        setupCoinoneMock("ADA");

        // when
        tickerService.getCoinoneTicker("ADA");

        // then
        verify(coinoneTickerApiClient, times(1)).getTicker("KRW", "ADA");
        verify(binanceTickerApiClient, never()).getTicker(anyString());
    }

    @Test
    @DisplayName("getBinanceTicker 호출 시 올바른 심볼로 Binance API를 호출한다")
    void should_CallBinanceApiWithCorrectSymbol_When_GetBinanceTicker() {
        // given
        setupBinanceMock("DOT");

        // when
        tickerService.getBinanceTicker("DOT");

        // then
        verify(binanceTickerApiClient, times(1)).getTicker("DOTUSDT");
        verify(coinoneTickerApiClient, never()).getTicker(anyString(), anyString());
    }

    @Test
    @DisplayName("Coinone API가 한 번만 호출되는지 검증한다")
    void should_CallCoinoneApiOnce_When_GetCoinoneTicker() {
        // given
        setupCoinoneMock("BTC");

        // when
        tickerService.getCoinoneTicker("BTC");

        // then
        verify(coinoneTickerApiClient, times(1)).getTicker("KRW", "BTC");
        verifyNoMoreInteractions(coinoneTickerApiClient);
    }

    @Test
    @DisplayName("Binance API가 한 번만 호출되는지 검증한다")
    void should_CallBinanceApiOnce_When_GetBinanceTicker() {
        // given
        setupBinanceMock("BTC");

        // when
        tickerService.getBinanceTicker("BTC");

        // then
        verify(binanceTickerApiClient, times(1)).getTicker("BTCUSDT");
        verifyNoMoreInteractions(binanceTickerApiClient);
    }

    @Test
    @DisplayName("대소문자가 섞인 통화 코드로 올바른 API 호출을 한다")
    void should_CallApiWithCorrectCase_When_CurrencyCodeHasMixedCase() {
        // given
        setupCoinoneMock("btc");
        setupBinanceMock("btc");

        // when
        tickerService.getCoinoneTicker("btc");
        tickerService.getBinanceTicker("btc");

        // then
        verify(coinoneTickerApiClient).getTicker("KRW", "btc");
        verify(binanceTickerApiClient).getTicker("btcUSDT");
    }

    @Test
    @DisplayName("Mock 객체가 null을 반환할 때 NullPointerException이 발생한다")
    void should_ThrowNullPointerException_When_MockReturnsNull() {
        // given
        given(coinoneTickerApiClient.getTicker(anyString(), anyString())).willReturn(null);

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(
            NullPointerException.class,
            () -> tickerService.getCoinoneTicker("BTC")
        );
    }

    @Test
    @DisplayName("getAllTickers에서 CompletableFuture가 올바르게 동작한다")
    void should_HandleCompletableFutureCorrectly_When_GetAllTickers() {
        // given
        setupCoinoneMock("ETH");
        setupBinanceMock("ETH");

        // when
        List<TickerDto> result = tickerService.getAllTickers("ETH");

        // then
        assertThat(result).hasSize(2);
        verify(coinoneTickerApiClient).getTicker("KRW", "ETH");
        verify(binanceTickerApiClient).getTicker("ETHUSDT");
    }

    private void setupDefaultMocks() {
        setupCoinoneMock("BTC");
        setupCoinoneMock("ETH");
        setupBinanceMock("BTC");
        setupBinanceMock("ETH");
    }

    private void setupCoinoneMock(String currency) {
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("KRW")
            .targetCurrency(currency)
            .first("94000000")
            .last("95000000")
            .high("96000000")
            .low("94000000")
            .targetVolume("1234.567")
            .bestBids(List.of(CoinoneTicker.BestOrderBook.builder()
                .price("94999999")
                .qty("1.0")
                .build()))
            .bestAsks(List.of(CoinoneTicker.BestOrderBook.builder()
                .price("95000001")
                .qty("1.0")
                .build()))
            .build();

        CoinoneTickerApiResponse response = CoinoneTickerApiResponse.builder()
            .result("success")
            .errorCode(null)
            .serverTime("1700000000000")
            .tickers(List.of(ticker))
            .build();

        given(coinoneTickerApiClient.getTicker("KRW", currency)).willReturn(response);
    }

    private void setupBinanceMock(String currency) {
        String symbol = currency + "USDT";
        
        BinanceTickerApiResponse response = BinanceTickerApiResponse.builder()
            .symbol(symbol)
            .lastPrice("67500.50")
            .highPrice("68000.00")
            .lowPrice("67000.00")
            .bidPrice("67500.40")
            .askPrice("67500.60")
            .priceChangePercent("1.25")
            .volume("1500.75")
            .build();

        given(binanceTickerApiClient.getTicker(symbol)).willReturn(response);
    }
}
