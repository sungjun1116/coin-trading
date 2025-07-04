package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceAccountApiResponse;

@Component
@HttpExchange
public interface BinanceAccountApiClient {

    @GetExchange("/api/v3/account")
    BinanceAccountApiResponse getAccount(@RequestParam(value = "omitZeroBalances", required = false) boolean omitZeroBalances,
                      @RequestParam(value = "recvWindow", required = false) long recvWindow,
                      @RequestParam("timestamp") long timestamp);

    default BinanceAccountApiResponse getAccount(boolean omitZeroBalances, long timestamp) {
        return getAccount(omitZeroBalances, 5000L, timestamp); // recvWindow 기본값 5000L
    }

    default BinanceAccountApiResponse getAccount(long recvWindow, long timestamp) {
        return getAccount(false, recvWindow, timestamp); // omitZeroBalances false
    }

    default BinanceAccountApiResponse getAccount(long timestamp) {
        return getAccount(false, 5000L, timestamp); // 기본값 설정
    }

}
