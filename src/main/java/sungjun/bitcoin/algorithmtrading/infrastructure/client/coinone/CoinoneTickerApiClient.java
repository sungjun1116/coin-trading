package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;

/**
 * Coinone 거래소의 시세 정보 조회 API 클라이언트입니다.
 * <p>
 * 이 클라이언트는 Coinone API의 ticker 엔드포인트와 통신하여
 * 암호화폐의 실시간 시세 정보를 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@HttpExchange
public interface CoinoneTickerApiClient {

    /**
     * 지정된 통화 쌍에 대한 실시간 시세 정보를 Coinone 거래소에서 조회합니다.
     *
     * @param quoteCurrency 기준 통화 코드 (예: KRW)
     * @param targetCurrency 대상 암호화폐 코드 (예: BTC)
     * @return 주어진 통화 쌍의 시세 데이터
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException API 호출 실패 시 발생
     */
    @GetExchange("/ticker_new/{quoteCurrency}/{targetCurrency}")
    CoinoneTickerApiResponse getTicker(@PathVariable String quoteCurrency, @PathVariable String targetCurrency);

}
