package sungjun.bitcoin.algorithmtrading.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.BinanceTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.service.dto.TickerDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TickerService 통합 테스트")
class TickerServiceTest {

    @Mock
    private CoinoneTickerApiClient coinoneTickerApiClient;

    @Mock
    private BinanceTickerApiClient binanceTickerApiClient;

    @InjectMocks
    private TickerService tickerService;

    @Test
    @DisplayName("인기 암호화폐 시세 정보를 모두 조회한다")
    void should_ReturnPopularTickers_When_GetPopularTickers() {
        // given
        setupCoinoneMock("BTC");
        setupCoinoneMock("ETH");
        setupBinanceMock("BTC");
        setupBinanceMock("ETH");

        // when
        List<TickerDto> tickers = tickerService.getPopularTickers();

        // then
        assertThat(tickers).hasSize(4);
        assertThat(tickers)
            .extracting(TickerDto::getExchange)
            .containsExactly("coinone", "coinone", "binance", "binance");
        assertThat(tickers)
            .extracting(TickerDto::getSymbol)
            .containsExactly("BTC/KRW", "ETH/KRW", "BTCUSDT", "ETHUSDT");
    }

    @Test
    @DisplayName("특정 통화의 모든 거래소 시세 정보를 조회한다")
    void should_ReturnAllTickers_When_GetAllTickers() {
        // given
        setupCoinoneMock("BTC");
        setupBinanceMock("BTC");

        // when
        List<TickerDto> tickers = tickerService.getAllTickers("BTC");

        // then
        assertThat(tickers).hasSize(2);
        assertThat(tickers)
            .extracting(TickerDto::getExchange)
            .containsExactlyInAnyOrder("coinone", "binance");
        
        TickerDto coinoneTicker = tickers.stream()
            .filter(ticker -> "coinone".equals(ticker.getExchange()))
            .findFirst()
            .orElseThrow();
        assertThat(coinoneTicker.getSymbol()).isEqualTo("BTC/KRW");

        TickerDto binanceTicker = tickers.stream()
            .filter(ticker -> "binance".equals(ticker.getExchange()))
            .findFirst()
            .orElseThrow();
        assertThat(binanceTicker.getSymbol()).isEqualTo("BTCUSDT");
    }

    @Test
    @DisplayName("Coinone 거래소의 BTC 시세 정보를 조회한다")
    void should_ReturnCoinoneTicker_When_GetCoinoneTicker() {
        // given
        setupCoinoneMock("BTC");

        // when
        TickerDto ticker = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(ticker.getExchange()).isEqualTo("coinone");
        assertThat(ticker.getSymbol()).isEqualTo("BTC/KRW");
        assertThat(ticker.getPrice()).isEqualByComparingTo(new BigDecimal("95000000"));
        assertThat(ticker.getHighPrice()).isEqualByComparingTo(new BigDecimal("96000000"));
        assertThat(ticker.getLowPrice()).isEqualByComparingTo(new BigDecimal("94000000"));
        assertThat(ticker.getBidPrice()).isEqualByComparingTo(new BigDecimal("94999999"));
        assertThat(ticker.getAskPrice()).isEqualByComparingTo(new BigDecimal("95000001"));
        assertThat(ticker.getVolume()).isEqualByComparingTo(new BigDecimal("1234.567"));
        assertThat(ticker.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Coinone 거래소의 ETH 시세 정보를 조회한다")
    void should_ReturnCoinoneTickerForETH_When_GetCoinoneTicker() {
        // given
        setupCoinoneMock("ETH");

        // when
        TickerDto ticker = tickerService.getCoinoneTicker("ETH");

        // then
        assertThat(ticker.getExchange()).isEqualTo("coinone");
        assertThat(ticker.getSymbol()).isEqualTo("ETH/KRW");
        verify(coinoneTickerApiClient).getTicker("KRW", "ETH");
    }

    @Test
    @DisplayName("Binance 거래소의 BTC 시세 정보를 조회한다")
    void should_ReturnBinanceTicker_When_GetBinanceTicker() {
        // given
        setupBinanceMock("BTC");

        // when
        TickerDto ticker = tickerService.getBinanceTicker("BTC");

        // then
        assertThat(ticker.getExchange()).isEqualTo("binance");
        assertThat(ticker.getSymbol()).isEqualTo("BTCUSDT");
        assertThat(ticker.getPrice()).isEqualByComparingTo(new BigDecimal("67500.50"));
        assertThat(ticker.getHighPrice()).isEqualByComparingTo(new BigDecimal("68000.00"));
        assertThat(ticker.getLowPrice()).isEqualByComparingTo(new BigDecimal("67000.00"));
        assertThat(ticker.getBidPrice()).isEqualByComparingTo(new BigDecimal("67500.40"));
        assertThat(ticker.getAskPrice()).isEqualByComparingTo(new BigDecimal("67500.60"));
        assertThat(ticker.getChangePercent()).isEqualByComparingTo(new BigDecimal("1.25"));
        assertThat(ticker.getVolume()).isEqualByComparingTo(new BigDecimal("1500.75"));
        assertThat(ticker.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Binance 거래소의 ETH 시세 정보를 조회한다")
    void should_ReturnBinanceTickerForETH_When_GetBinanceTicker() {
        // given
        setupBinanceMock("ETH");

        // when
        TickerDto ticker = tickerService.getBinanceTicker("ETH");

        // then
        assertThat(ticker.getExchange()).isEqualTo("binance");
        assertThat(ticker.getSymbol()).isEqualTo("ETHUSDT");
        verify(binanceTickerApiClient).getTicker("ETHUSDT");
    }

    @Test
    @DisplayName("Coinone API에서 빈 BestBids 리스트를 반환할 때 null bidPrice를 설정한다")
    void should_SetNullBidPrice_When_CoinoneReturnsEmptyBestBids() {
        // given
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("KRW")
            .targetCurrency("BTC")
            .first("94000000")
            .last("95000000")
            .high("96000000")
            .low("94000000")
            .targetVolume("1234.567")
            .bestBids(List.of()) // 빈 리스트
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

        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(response);

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getBidPrice()).isNull();
        assertThat(result.getAskPrice()).isNotNull();
    }

    @Test
    @DisplayName("Coinone API에서 빈 BestAsks 리스트를 반환할 때 null askPrice를 설정한다")
    void should_SetNullAskPrice_When_CoinoneReturnsEmptyBestAsks() {
        // given
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("KRW")
            .targetCurrency("BTC")
            .first("94000000")
            .last("95000000")
            .high("96000000")
            .low("94000000")
            .targetVolume("1234.567")
            .bestBids(List.of(CoinoneTicker.BestOrderBook.builder()
                .price("94999999")
                .qty("1.0")
                .build()))
            .bestAsks(List.of()) // 빈 리스트
            .build();

        CoinoneTickerApiResponse response = CoinoneTickerApiResponse.builder()
            .result("success")
            .errorCode(null)
            .serverTime("1700000000000")
            .tickers(List.of(ticker))
            .build();

        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(response);

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getBidPrice()).isNotNull();
        assertThat(result.getAskPrice()).isNull();
    }

    @Test
    @DisplayName("Coinone API에서 null 값들을 반환할 때 기본값을 설정한다")
    void should_SetDefaultValues_When_CoinoneReturnsNullValues() {
        // given
        CoinoneTicker ticker = CoinoneTicker.builder()
            .quoteCurrency("KRW")
            .targetCurrency("BTC")
            .first(null)
            .last(null)
            .high(null)
            .low(null)
            .targetVolume(null)
            .bestBids(List.of())
            .bestAsks(List.of())
            .build();

        CoinoneTickerApiResponse response = CoinoneTickerApiResponse.builder()
            .result("success")
            .errorCode(null)
            .serverTime("1700000000000")
            .tickers(List.of(ticker))
            .build();

        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(response);

        // when
        TickerDto result = tickerService.getCoinoneTicker("BTC");

        // then
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getHighPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getLowPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getBidPrice()).isNull();
        assertThat(result.getAskPrice()).isNull();
        assertThat(result.getChangePercent()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getVolume()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("API 호출이 예외를 발생시킬 때 예외가 전파된다")
    void should_PropagateException_When_ApiCallFails() {
        // given
        when(coinoneTickerApiClient.getTicker("KRW", "BTC"))
            .thenThrow(new RuntimeException("API 호출 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getCoinoneTicker("BTC"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("API 호출 실패");
    }

    private void setupCoinoneMock(String currency) {
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
            .targetCurrency(currency)
            .first("94000000")
            .last("95000000")
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
