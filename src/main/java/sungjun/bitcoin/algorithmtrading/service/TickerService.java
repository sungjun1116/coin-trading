package sungjun.bitcoin.algorithmtrading.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.BinanceTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.service.dto.TickerDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 실시간 시세 정보를 조회하고 관리하는 서비스 클래스입니다.
 * <p>
 * 여러 거래소의 시세 정보를 통합하여 표준화된 형태로 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class TickerService {

    private final CoinoneTickerApiClient coinoneTickerApiClient;
    private final BinanceTickerApiClient binanceTickerApiClient;

    /**
     * 인기 암호화폐(BTC, ETH)의 시세 정보를 조회합니다.
     *
     * @return 인기 암호화폐 시세 정보 리스트 (BTC, ETH의 KRW, USDT 시세)
     */
    public List<TickerDto> getPopularTickers() {
        return List.of(
            getCoinoneTicker("BTC"),
            getCoinoneTicker("ETH"),
            getBinanceTicker("BTC"),
            getBinanceTicker("ETH")
        );
    }

    /**
     * 여러 거래소의 동일한 통화에 대한 시세 정보를 모든 심볼로 조회합니다.
     *
     * @param targetCurrency 대상 통화 (예: "BTC", "ETH")
     * @return 모든 거래소의 시세 정보 리스트 (KRW, USDT 모두 포함)
     */
    public List<TickerDto> getAllTickers(String targetCurrency) {
        CompletableFuture<TickerDto> coinoneKrwFuture = CompletableFuture.supplyAsync(() ->
            getCoinoneTicker(targetCurrency)
        );

        CompletableFuture<TickerDto> binanceUsdtFuture = CompletableFuture.supplyAsync(() ->
            getBinanceTicker(targetCurrency)
        );

        return CompletableFuture.allOf(coinoneKrwFuture, binanceUsdtFuture)
            .thenApply(v -> List.of(coinoneKrwFuture.join(), binanceUsdtFuture.join()))
            .join();
    }

    /**
     * Coinone 거래소의 특정 심볼에 대한 시세 정보를 조회합니다.
     *
     * @param targetCurrency 대상 통화 (예: "BTC", "ETH")
     * @param symbol 통화 심볼 (예: "KRW")
     * @return 표준화된 시세 정보 DTO
     */
    public TickerDto getCoinoneTicker(String targetCurrency) {
        // Coinone API 호출
        CoinoneTickerApiResponse response = coinoneTickerApiClient.getTicker("KRW", targetCurrency);
        CoinoneTicker ticker = response.getTickers().getFirst();
        
        // 표준화된 심볼 형식 생성
        String standardSymbol = targetCurrency + "/KRW";
        
        return TickerDto.builder()
            .exchange("coinone")
            .symbol(standardSymbol)
            .price(ticker.getLast() != null ? new BigDecimal(ticker.getLast()) : BigDecimal.ZERO)
            .highPrice(ticker.getHigh() != null ? new BigDecimal(ticker.getHigh()) : BigDecimal.ZERO)
            .lowPrice(ticker.getLow() != null ? new BigDecimal(ticker.getLow()) : BigDecimal.ZERO)
            .bidPrice(ticker.getBestBids().isEmpty() ? null : new BigDecimal(ticker.getBestBids().getFirst().getPrice()))
            .askPrice(ticker.getBestAsks().isEmpty() ? null : new BigDecimal(ticker.getBestAsks().getFirst().getPrice()))
            .changePercent(calculateChangePercent(ticker.getFirst(), ticker.getLast()))
            .volume(ticker.getTargetVolume() != null ? new BigDecimal(ticker.getTargetVolume()) : BigDecimal.ZERO)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Binance 거래소의 특정 심볼에 대한 시세 정보를 조회합니다.
     *
     * @param targetCurrency 대상 통화 (예: "BTC", "ETH")
     * @return 표준화된 시세 정보 DTO
     */
    public TickerDto getBinanceTicker(String targetCurrency) {
        // Binance API용 심볼 생성 (예: "BTCUSDT")
        String binanceSymbol = targetCurrency + "USDT";
        
        // Binance API 호출
        BinanceTickerApiResponse response = binanceTickerApiClient.getTicker(binanceSymbol);
        
        return TickerDto.builder()
            .exchange("binance")
            .symbol(binanceSymbol)
            .price(new BigDecimal(response.getLastPrice()))
            .highPrice(new BigDecimal(response.getHighPrice()))
            .lowPrice(new BigDecimal(response.getLowPrice()))
            .bidPrice(new BigDecimal(response.getBidPrice()))
            .askPrice(new BigDecimal(response.getAskPrice()))
            .changePercent(new BigDecimal(response.getPriceChangePercent()))
            .volume(new BigDecimal(response.getVolume()))
            .timestamp(LocalDateTime.now())
            .build();
    }


    /**
     * 시작가와 현재가를 기준으로 변동률을 계산합니다.
     *
     * @param firstPrice 시작가
     * @param lastPrice 현재가
     * @return 변동률 (%)
     */
    private BigDecimal calculateChangePercent(String firstPrice, String lastPrice) {
        if (firstPrice == null || lastPrice == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal first = new BigDecimal(firstPrice);
        BigDecimal last = new BigDecimal(lastPrice);
        
        if (first.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        
        return last.subtract(first)
            .divide(first, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }
}
