package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BinanceBalance {

    private String asset;
    private String free;
    private String locked;
    
    @Builder
    private BinanceBalance(String asset, String free, String locked) {
        this.asset = asset;
        this.free = free;
        this.locked = locked;
    }
}
