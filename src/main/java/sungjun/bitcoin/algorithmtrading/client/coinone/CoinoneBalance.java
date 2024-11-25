package sungjun.bitcoin.algorithmtrading.client.coinone;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CoinoneBalance {

    private String available;

    private String limit;

    private String averagePrice;

    private String currency;

    @Builder
    private CoinoneBalance(String available, String limit, String averagePrice, String currency) {
        this.available = available;
        this.limit = limit;
        this.averagePrice = averagePrice;
        this.currency = currency;
    }
}
