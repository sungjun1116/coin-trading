package sungjun.bitcoin.algorithmtrading.client.coinone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Getter
    static class BestOrderBook {
        private final String price;
        private final String qty;

        @Builder
        private BestOrderBook(String price, String qty) {
            this.price = price;
            this.qty = qty;
        }
    }
}
