package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response.BinanceTickerApiResponse;

/**
 * Binance 거래소의 시세 정보 조회 API 클라이언트입니다.
 * <p>
 * 이 클라이언트는 Binance API의 ticker 엔드포인트와 통신하여
 * 암호화폐의 실시간 시세 정보를 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@HttpExchange
public interface BinanceTickerApiClient {

    /**
     * Binance 거래소에서 지정된 심볼에 대한 실시간 시세 정보를 조회합니다.
     *
     * @param symbol 거래 쌍 심볼 (예: "BTCUSDT")
     * @return 주어진 심볼에 대한 시세 데이터
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.binance.BinanceApiException API 호출 실패 시 발생
     */
    @GetExchange("/api/v3/ticker/24hr")
    BinanceTickerApiResponse getTicker(@RequestParam String symbol);

}
