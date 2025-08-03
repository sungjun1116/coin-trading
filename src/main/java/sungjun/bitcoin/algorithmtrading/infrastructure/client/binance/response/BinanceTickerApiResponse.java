package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Binance 24hr Ticker Price Change Statistics API 응답을 위한 DTO 클래스입니다.
 * <p>
 * 24시간 동안의 가격 변동 통계 정보를 포함합니다.
 * Binance REST API v3의 /api/v3/ticker/24hr 엔드포인트 응답을 매핑합니다.
 * </p>
 * 
 * @author sungjun
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class BinanceTickerApiResponse {

    /**
     * 심볼 (예: BTCUSDT)
     */
    private String symbol;

    /**
     * 가격 변화량
     */
    private String priceChange;

    /**
     * 가격 변화율 (백분율)
     */
    private String priceChangePercent;

    /**
     * 가중 평균 가격
     */
    private String weightedAvgPrice;

    /**
     * 이전 종가 (24시간 전)
     */
    private String prevClosePrice;

    /**
     * 최신 가격
     */
    private String lastPrice;

    /**
     * 최신 수량
     */
    private String lastQty;

    /**
     * 최적 매수 호가
     */
    private String bidPrice;

    /**
     * 최적 매수 호가 수량
     */
    private String bidQty;

    /**
     * 최적 매도 호가
     */
    private String askPrice;

    /**
     * 최적 매도 호가 수량
     */
    private String askQty;

    /**
     * 시가 (24시간 전)
     */
    private String openPrice;

    /**
     * 고가 (24시간 중)
     */
    private String highPrice;

    /**
     * 저가 (24시간 중)
     */
    private String lowPrice;

    /**
     * 거래량 (기준 자산)
     */
    private String volume;

    /**
     * 거래량 (호가 자산)
     */
    private String quoteVolume;

    /**
     * 통계 시작 시간 (밀리초 타임스탬프)
     */
    private long openTime;

    /**
     * 통계 종료 시간 (밀리초 타임스탬프)
     */
    private long closeTime;

    /**
     * 첫 번째 거래 ID
     */
    private long firstId;

    /**
     * 마지막 거래 ID
     */
    private long lastId;

    /**
     * 거래 횟수
     */
    private long count;

    @Builder
    private BinanceTickerApiResponse(String symbol, String priceChange, String priceChangePercent,
                                   String weightedAvgPrice, String prevClosePrice, String lastPrice,
                                   String lastQty, String bidPrice, String bidQty, String askPrice,
                                   String askQty, String openPrice, String highPrice, String lowPrice,
                                   String volume, String quoteVolume, long openTime, long closeTime,
                                   long firstId, long lastId, long count) {
        this.symbol = symbol;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.weightedAvgPrice = weightedAvgPrice;
        this.prevClosePrice = prevClosePrice;
        this.lastPrice = lastPrice;
        this.lastQty = lastQty;
        this.bidPrice = bidPrice;
        this.bidQty = bidQty;
        this.askPrice = askPrice;
        this.askQty = askQty;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
        this.quoteVolume = quoteVolume;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.firstId = firstId;
        this.lastId = lastId;
        this.count = count;
    }
}
