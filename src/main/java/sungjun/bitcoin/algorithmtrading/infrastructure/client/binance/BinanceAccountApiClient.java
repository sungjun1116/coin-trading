package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceAccountApiResponse;

/**
 * Binance 거래소의 계정 정보 조회 API 클라이언트입니다.
 * <p>
 * 이 클라이언트는 Binance API의 account 엔드포인트와 통신하여
 * 사용자의 계정 잔고 및 자산 정보를 제공합니다.
 * 모든 API 호출은 인증이 필요합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@HttpExchange
public interface BinanceAccountApiClient {

    /****
                       * Retrieves detailed account information from Binance, including balances and trading fee details.
                       *
                       * @param omitZeroBalances whether to exclude assets with zero balance from the response
                       * @param recvWindow the validity window for the request in milliseconds (maximum 60000)
                       * @param timestamp the Unix timestamp in milliseconds representing the request time
                       * @return the account information response from Binance
                       * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.binance.BinanceApiException if the API call fails
                       */
    @GetExchange("/api/v3/account")
    BinanceAccountApiResponse getAccount(@RequestParam(value = "omitZeroBalances", required = false) boolean omitZeroBalances,
                      @RequestParam(value = "recvWindow", required = false) long recvWindow,
                      @RequestParam("timestamp") long timestamp);

    /**
     * Retrieves account information from Binance with an option to omit zero balances, using a default request validity window of 5000 milliseconds.
     *
     * @param omitZeroBalances whether to exclude assets with zero balance from the response
     * @param timestamp the Unix timestamp (in milliseconds) representing the request time
     * @return the account information response from Binance
     */
    default BinanceAccountApiResponse getAccount(boolean omitZeroBalances, long timestamp) {
        return getAccount(omitZeroBalances, 5000L, timestamp); // recvWindow 기본값 5000L
    }

    /**
     * Retrieves account information from Binance with all asset balances included.
     *
     * The request uses the specified validity window and timestamp. All assets are included, even those with zero balances.
     *
     * @param recvWindow The request validity window in milliseconds (maximum 60000).
     * @param timestamp The Unix timestamp in milliseconds representing the request time.
     * @return The account information response from Binance.
     */
    default BinanceAccountApiResponse getAccount(long recvWindow, long timestamp) {
        return getAccount(false, recvWindow, timestamp); // omitZeroBalances false
    }

    /**
     * Retrieves account information using the specified timestamp, including all assets and a default request window of 5000 ms.
     *
     * @param timestamp Unix timestamp in milliseconds representing the request time.
     * @return BinanceAccountApiResponse containing account details.
     */
    default BinanceAccountApiResponse getAccount(long timestamp) {
        return getAccount(false, 5000L, timestamp); // 기본값 설정
    }

}
