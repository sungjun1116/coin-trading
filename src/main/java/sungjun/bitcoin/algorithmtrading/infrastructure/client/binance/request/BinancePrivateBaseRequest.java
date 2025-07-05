package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BinancePrivateBaseRequest {

    protected long recvWindow;

    protected long timestamp;

    protected BinancePrivateBaseRequest(long recvWindow, long timestamp) {
        this.recvWindow = recvWindow;
        this.timestamp = timestamp;
    }
}
