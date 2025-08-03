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

import java.util.List;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TickerService 예외 상황 테스트")
class TickerServiceExceptionTest {

    @Mock
    private CoinoneTickerApiClient coinoneTickerApiClient;

    @Mock
    private BinanceTickerApiClient binanceTickerApiClient;

    @InjectMocks
    private TickerService tickerService;

    @Test
    @DisplayName("Coinone API 호출 실패 시 RuntimeException이 전파된다")
    void should_PropagateRuntimeException_When_CoinoneApiCallFails() {
        // given
        given(coinoneTickerApiClient.getTicker("KRW", "BTC"))
            .willThrow(new RuntimeException("Coinone API 연결 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getCoinoneTicker("BTC"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Coinone API 연결 실패");
    }

    @Test
    @DisplayName("Binance API 호출 실패 시 RuntimeException이 전파된다")
    void should_PropagateRuntimeException_When_BinanceApiCallFails() {
        // given
        given(binanceTickerApiClient.getTicker("BTCUSDT"))
            .willThrow(new RuntimeException("Binance API 연결 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getBinanceTicker("BTC"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Binance API 연결 실패");
    }

    @Test
    @DisplayName("getPopularTickers에서 Coinone API 실패 시 예외가 전파된다")
    void should_PropagateException_When_CoinoneFailsInGetPopularTickers() {
        // given
        given(coinoneTickerApiClient.getTicker("KRW", "BTC"))
            .willThrow(new RuntimeException("Coinone BTC API 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getPopularTickers())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Coinone BTC API 실패");
    }

    @Test
    @DisplayName("getPopularTickers에서 Binance API 실패 시 예외가 전파된다")
    void should_PropagateException_When_BinanceFailsInGetPopularTickers() {
        // given
        setupSuccessfulCoinoneMocks();
        given(binanceTickerApiClient.getTicker("BTCUSDT"))
            .willThrow(new RuntimeException("Binance BTC API 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getPopularTickers())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Binance BTC API 실패");
    }

    @Test
    @DisplayName("getAllTickers에서 Coinone API 실패 시 CompletionException이 발생한다")
    void should_ThrowCompletionException_When_CoinoneFailsInGetAllTickers() {
        // given
        given(coinoneTickerApiClient.getTicker("KRW", "BTC"))
            .willThrow(new RuntimeException("Coinone API 실패"));
        setupSuccessfulBinanceMock("BTC");

        // when & then
        assertThatThrownBy(() -> tickerService.getAllTickers("BTC"))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("getAllTickers에서 Binance API 실패 시 CompletionException이 발생한다")
    void should_ThrowCompletionException_When_BinanceFailsInGetAllTickers() {
        // given
        setupSuccessfulCoinoneMock("BTC");
        given(binanceTickerApiClient.getTicker("BTCUSDT"))
            .willThrow(new RuntimeException("Binance API 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getAllTickers("BTC"))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("getAllTickers에서 양쪽 API 모두 실패 시 첫 번째 예외가 발생한다")
    void should_ThrowFirstException_When_BothApisFailInGetAllTickers() {
        // given
        given(coinoneTickerApiClient.getTicker("KRW", "BTC"))
            .willThrow(new RuntimeException("Coinone API 실패"));
        given(binanceTickerApiClient.getTicker("BTCUSDT"))
            .willThrow(new RuntimeException("Binance API 실패"));

        // when & then
        assertThatThrownBy(() -> tickerService.getAllTickers("BTC"))
            .isInstanceOf(CompletionException.class);
    }

    @Test
    @DisplayName("Coinone API에서 빈 ticker 리스트를 반환할 때 NoSuchElementException이 발생한다")
    void should_ThrowNoSuchElementException_When_CoinoneReturnsEmptyTickerList() {
        // given
        CoinoneTickerApiResponse response = CoinoneTickerApiResponse.builder()
            .result("success")
            .errorCode(null)
            .serverTime("1700000000000")
            .tickers(List.of()) // 빈 리스트
            .build();

        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(response);

        // when & then
        assertThatThrownBy(() -> tickerService.getCoinoneTicker("BTC"))
            .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    @DisplayName("API 타임아웃 시 예외가 전파된다")
    void should_PropagateTimeoutException_When_ApiCallTimesOut() {
        // given
        given(coinoneTickerApiClient.getTicker(anyString(), anyString()))
            .willThrow(new RuntimeException("연결 시간 초과"));

        // when & then
        assertThatThrownBy(() -> tickerService.getCoinoneTicker("BTC"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("연결 시간 초과");
    }

    @Test
    @DisplayName("잘못된 JSON 파싱 시 예외가 전파된다")
    void should_PropagateParsingException_When_JsonParsingFails() {
        // given
        given(binanceTickerApiClient.getTicker("BTCUSDT"))
            .willThrow(new RuntimeException("JSON 파싱 오류"));

        // when & then
        assertThatThrownBy(() -> tickerService.getBinanceTicker("BTC"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("JSON 파싱 오류");
    }

    @Test
    @DisplayName("NumberFormatException 발생 시 예외가 전파된다")
    void should_PropagateNumberFormatException_When_InvalidNumberFormat() {
        // given
        BinanceTickerApiResponse response = BinanceTickerApiResponse.builder()
            .symbol("BTCUSDT")
            .lastPrice("invalid_number")
            .highPrice("68000.00")
            .lowPrice("67000.00")
            .bidPrice("67500.40")
            .askPrice("67500.60")
            .priceChangePercent("1.25")
            .volume("1500.75")
            .build();

        given(binanceTickerApiClient.getTicker("BTCUSDT")).willReturn(response);

        // when & then
        assertThatThrownBy(() -> tickerService.getBinanceTicker("BTC"))
            .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("null 응답을 받을 때 NullPointerException이 발생한다")
    void should_ThrowNullPointerException_When_ApiReturnsNull() {
        // given
        given(coinoneTickerApiClient.getTicker("KRW", "BTC")).willReturn(null);

        // when & then
        assertThatThrownBy(() -> tickerService.getCoinoneTicker("BTC"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("일부 API 실패해도 성공한 API의 결과는 정상 반환된다")
    void should_ReturnSuccessfulResults_When_OneApiFailsInPartialOperation() {
        // given
        setupSuccessfulCoinoneMock("ETH");
        given(binanceTickerApiClient.getTicker("ETHUSDT"))
            .willThrow(new RuntimeException("Binance API 실패"));

        // 개별 호출은 성공해야 함
        TickerDto coinoneResult = tickerService.getCoinoneTicker("ETH");
        assertThat(coinoneResult).isNotNull();
        assertThat(coinoneResult.getExchange()).isEqualTo("coinone");

        // getAllTickers는 실패해야 함
        assertThatThrownBy(() -> tickerService.getAllTickers("ETH"))
            .isInstanceOf(CompletionException.class);
    }

    private void setupSuccessfulCoinoneMocks() {
        setupSuccessfulCoinoneMock("BTC");
        setupSuccessfulCoinoneMock("ETH");
    }

    private void setupSuccessfulCoinoneMock(String currency) {
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

    private void setupSuccessfulBinanceMock(String currency) {
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
