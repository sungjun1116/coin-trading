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
public final class CoinoneTiker {
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
     * Creates a CoinoneTiker instance containing ticker and order book data for a specific currency pair.
     *
     * @param quoteCurrency the symbol of the quote currency
     * @param targetCurrency the symbol of the target currency
     * @param timeStamp the timestamp representing when the ticker data was recorded
     * @param high the highest price during the period, as a string
     * @param low the lowest price during the period, as a string
     * @param first the first traded price in the period, as a string
     * @param last the last traded price in the period, as a string
     * @param quoteVolume the traded volume in the quote currency, as a string
     * @param targetVolume the traded volume in the target currency, as a string
     * @param bestAsks the list of best ask order book entries
     * @param bestBids the list of best bid order book entries
     * @param id the unique identifier for this ticker data
     */
    @Builder
    private CoinoneTiker(
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
    static class BestOrderBook {
        /**
         * 호가 가격 (문자열 형태)
         */
        private final String price;
        
        /**
         * 호가 수량 (문자열 형태)
         */
        private final String qty;

        /**
         * Creates a BestOrderBook entry with the given price and quantity.
         *
         * @param price the string representation of the order price
         * @param qty the string representation of the order quantity
         */
        @Builder
        private BestOrderBook(String price, String qty) {
            this.price = price;
            this.qty = qty;
        }
    }
}
