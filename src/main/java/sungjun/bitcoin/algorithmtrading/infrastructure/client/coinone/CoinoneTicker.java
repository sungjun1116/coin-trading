package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Coinone 거래소의 시세 정보를 나타내는 데이터 클래스입니다.
 * <p>
 * 특정 통화쌍에 대한 실시간 시세 정보와 호가 정보를 포함합니다.
 * JSON 응답에서 snake_case 형태의 필드명을 camelCase로 매핑합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class CoinoneTicker {
    private String quoteCurrency;
    private String targetCurrency;
    private int timeStamp;
    private String high;
    private String low;
    private String first;
    private String last;
    private String quoteVolume;
    private String targetVolume;
    private List<BestOrderBook> bestAsks;
    private List<BestOrderBook> bestBids;
    private String id;

    /**
     * Constructs a CoinoneTicker instance with the specified ticker and order book data for a currency pair.
     *
     * @param quoteCurrency   the quote currency symbol
     * @param targetCurrency  the target currency symbol
     * @param timeStamp       the timestamp of the ticker data
     * @param high            the highest price in the period
     * @param low             the lowest price in the period
     * @param first           the first traded price in the period
     * @param last            the last traded price in the period
     * @param quoteVolume     the traded volume in quote currency
     * @param targetVolume    the traded volume in target currency
     * @param bestAsks        the list of best ask order book entries
     * @param bestBids        the list of best bid order book entries
     * @param id              the unique identifier for this ticker data
     */
    @Builder
    private CoinoneTicker(
        String quoteCurrency,
        String targetCurrency,
        int timeStamp,
        String high,
        String low,
        String first,
        String last,
        String quoteVolume,
        String targetVolume,
        List<BestOrderBook> bestAsks,
        List<BestOrderBook> bestBids,
        String id
    ) {
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
        this.timeStamp = timeStamp;
        this.high = high;
        this.low = low;
        this.first = first;
        this.last = last;
        this.quoteVolume = quoteVolume;
        this.targetVolume = targetVolume;
        this.bestAsks = bestAsks;
        this.bestBids = bestBids;
        this.id = id;
    }

    /**
     * 최적 호가 정보를 나타내는 내부 클래스입니다.
     * <p>
     * 매수/매도 호가의 가격과 수량 정보를 포함합니다.
     * </p>
     */
    @Getter
    @NoArgsConstructor
    static class BestOrderBook {
        /**
         * 호가 가격 (문자열 형태)
         */
        private String price;
        
        /**
         * 호가 수량 (문자열 형태)
         */
        private String qty;

        /**
         * Constructs a BestOrderBook entry with the specified price and quantity.
         *
         * @param price the price of the order as a string
         * @param qty the quantity of the order as a string
         */
        @Builder
        private BestOrderBook(String price, String qty) {
            this.price = price;
            this.qty = qty;
        }
    }
}
