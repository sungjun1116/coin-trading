package sungjun.bitcoin.algorithmtrading.client.binance.request;

import lombok.Builder;

public class BinanceAccountRequest extends BinancePrivateBaseRequest{

    private boolean omitZeroBalances;

    @Builder
    private BinanceAccountRequest(boolean omitZeroBalances, long recvWindow, long timestamp) {
        super(recvWindow, timestamp);
        this.omitZeroBalances = omitZeroBalances;
    }

}
