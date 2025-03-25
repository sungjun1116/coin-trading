package sungjun.bitcoin.algorithmtrading.client.binance.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BinanceCommissionRates {

    private String maker;
    private String taker;
    private String buyer;
    private String seller;
    
    @Builder
    private BinanceCommissionRates(String maker, String taker, String buyer, String seller) {
        this.maker = maker;
        this.taker = taker;
        this.buyer = buyer;
        this.seller = seller;
    }
}
