package sungjun.bitcoin.algorithmtrading.service;

import org.springframework.stereotype.Service;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTicker;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;

/**
 * Coinone 거래소와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * <p>
 * 이 서비스는 Coinone API 클라이언트를 통해 시세 정보, 주문 관리 등의
 * 암호화폐 거래 관련 기능을 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Service
public class CoinoneService {

    private final CoinoneTickerApiClient coinoneTickerApiClient;

    /**
     * Constructs a CoinoneService with the specified CoinoneTickerApiClient.
     *
     * @param coinoneTickerApiClient the API client used to interact with the Coinone ticker service
     */
    public CoinoneService(CoinoneTickerApiClient coinoneTickerApiClient) {
        this.coinoneTickerApiClient = coinoneTickerApiClient;
    }

    /**
     * Retrieves the latest ticker information for the specified currency pair from the Coinone exchange.
     *
     * @param quoteCurrency the base currency (e.g., "KRW")
     * @param targetCurrency the target currency (e.g., "BTC")
     * @return the first {@link CoinoneTicker} object from the API response representing the current ticker data
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    public CoinoneTicker getTicker(String quoteCurrency, String targetCurrency) {
        CoinoneTickerApiResponse apiResponse = coinoneTickerApiClient.getTicker(quoteCurrency, targetCurrency);
        return apiResponse.getTickers().getFirst();
    }

}
