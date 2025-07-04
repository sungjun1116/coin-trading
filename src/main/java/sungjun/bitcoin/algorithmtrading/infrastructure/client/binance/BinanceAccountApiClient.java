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
     * 계정 정보를 조회합니다 (전체 파라미터 버전).
     * <p>
     * 사용자의 계정 잔고, 거래 수수료 정보 등을 포함한 상세 계정 정보를 조회합니다.
     * </p>
     *
     * @param omitZeroBalances 잔고가 0인 자산을 응답에서 제외할지 여부
     * @param recvWindow 요청 유효 시간 (밀리초, 최대 60000)
     * @param timestamp 요청 시각 (밀리초 단위 Unix timestamp)
     * @return {@link BinanceAccountApiResponse} 계정 정보 응답
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.binance.BinanceApiException API 호출 실패 시
     */
    @GetExchange("/api/v3/account")
    BinanceAccountApiResponse getAccount(@RequestParam(value = "omitZeroBalances", required = false) boolean omitZeroBalances,
                      @RequestParam(value = "recvWindow", required = false) long recvWindow,
                      @RequestParam("timestamp") long timestamp);

    /**
     * 계정 정보를 조회합니다 (잔고 필터링 및 타임스탬프 지정).
     * <p>
     * recvWindow는 기본값 5000ms로 설정됩니다.
     * </p>
     *
     * @param omitZeroBalances 잔고가 0인 자산을 응답에서 제외할지 여부
     * @param timestamp 요청 시각 (밀리초 단위 Unix timestamp)
     * @return {@link BinanceAccountApiResponse} 계정 정보 응답
     */
    default BinanceAccountApiResponse getAccount(boolean omitZeroBalances, long timestamp) {
        return getAccount(omitZeroBalances, 5000L, timestamp); // recvWindow 기본값 5000L
    }

    /**
     * 계정 정보를 조회합니다 (수신 윈도우 및 타임스탬프 지정).
     * <p>
     * omitZeroBalances는 false로 설정되어 모든 자산 정보를 포함합니다.
     * </p>
     *
     * @param recvWindow 요청 유효 시간 (밀리초, 최대 60000)
     * @param timestamp 요청 시각 (밀리초 단위 Unix timestamp)
     * @return {@link BinanceAccountApiResponse} 계정 정보 응답
     */
    default BinanceAccountApiResponse getAccount(long recvWindow, long timestamp) {
        return getAccount(false, recvWindow, timestamp); // omitZeroBalances false
    }

    /**
     * 계정 정보를 조회합니다 (타임스탬프만 지정).
     * <p>
     * 기본 설정으로 모든 자산 정보를 포함하고 recvWindow는 5000ms로 설정됩니다.
     * </p>
     *
     * @param timestamp 요청 시각 (밀리초 단위 Unix timestamp)
     * @return {@link BinanceAccountApiResponse} 계정 정보 응답
     */
    default BinanceAccountApiResponse getAccount(long timestamp) {
        return getAccount(false, 5000L, timestamp); // 기본값 설정
    }

}
