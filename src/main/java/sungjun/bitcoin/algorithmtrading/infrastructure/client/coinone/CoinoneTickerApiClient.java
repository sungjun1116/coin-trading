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
     * Fetches the latest ticker data for a given currency pair from the Coinone exchange.
     *
     * @param quoteCurrency the base currency code (e.g., KRW)
     * @param targetCurrency the target cryptocurrency code (e.g., BTC)
     * @return the real-time ticker information for the specified currency pair
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API request fails
     */
    @GetExchange("/ticker_new/{quoteCurrency}/{targetCurrency}")
    CoinoneTickerApiResponse getTicker(@PathVariable String quoteCurrency, @PathVariable String targetCurrency);

}
