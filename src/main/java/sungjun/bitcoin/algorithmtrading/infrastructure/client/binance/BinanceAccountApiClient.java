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

    /**
                       * Retrieves account information from Binance, including balances and trading fee details.
                       *
                       * @param omitZeroBalances if true, assets with zero balance are excluded from the response
                       * @param recvWindow the time window in milliseconds during which the request is valid (maximum 60000)
                       * @param timestamp the Unix timestamp in milliseconds indicating the request time
                       * @return the account information returned by Binance
                       * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.binance.BinanceApiException if the API call fails
                       */
    @GetExchange("/api/v3/account")
    BinanceAccountApiResponse getAccount(@RequestParam(value = "omitZeroBalances", required = false) boolean omitZeroBalances,
                      @RequestParam(value = "recvWindow", required = false) long recvWindow,
                      @RequestParam("timestamp") long timestamp);

    /**
     * Retrieves Binance account information, optionally omitting zero-balance assets, using a default request validity window of 5000 milliseconds.
     *
     * @param omitZeroBalances true to exclude assets with zero balance; false to include all assets
     * @param timestamp the Unix timestamp (in milliseconds) for the request
     * @return the account information response from Binance
     */
    default BinanceAccountApiResponse getAccount(boolean omitZeroBalances, long timestamp) {
        return getAccount(omitZeroBalances, 5000L, timestamp); // recvWindow 기본값 5000L
    }

    /**
     * Retrieves Binance account information including all asset balances.
     *
     * This overload includes assets with zero balances and allows specifying the request validity window and timestamp.
     *
     * @param recvWindow The request validity window in milliseconds (maximum 60000).
     * @param timestamp The Unix timestamp in milliseconds for the request.
     * @return The account information response from Binance.
     */
    default BinanceAccountApiResponse getAccount(long recvWindow, long timestamp) {
        return getAccount(false, recvWindow, timestamp); // omitZeroBalances false
    }

    /**
     * Retrieves account information for the specified timestamp, including all assets, using a default request window of 5000 milliseconds.
     *
     * @param timestamp the Unix timestamp in milliseconds indicating the request time.
     * @return the account details as a BinanceAccountApiResponse.
     */
    default BinanceAccountApiResponse getAccount(long timestamp) {
        return getAccount(false, 5000L, timestamp); // 기본값 설정
    }

}
