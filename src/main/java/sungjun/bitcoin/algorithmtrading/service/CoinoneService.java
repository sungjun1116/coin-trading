package sungjun.bitcoin.algorithmtrading.service;

import org.springframework.stereotype.Service;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTiker;

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
     * CoinoneService 생성자입니다.
     *
     * @param coinoneTickerApiClient Coinone Ticker API 클라이언트
     */
    public CoinoneService(CoinoneTickerApiClient coinoneTickerApiClient) {
        this.coinoneTickerApiClient = coinoneTickerApiClient;
    }

    /**
     * 지정된 통화쌍의 현재 시세 정보를 조회합니다.
     * <p>
     * Coinone API를 통해 실시간 시세 정보를 가져와서 첫 번째 티커 데이터를 반환합니다.
     * </p>
     *
     * @param quoteCurrency 기준 통화 (예: KRW)
     * @param targetCurrency 대상 통화 (예: BTC)
     * @return {@link CoinoneTiker} 시세 정보 객체
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.exception.CoinoneApiException API 호출 실패 시
     */
    public CoinoneTiker getTicker(String quoteCurrency, String targetCurrency) {
        CoinoneTickerApiResponse apiResponse = coinoneTickerApiClient.getTicker(quoteCurrency, targetCurrency);
        return apiResponse.getTickers().getFirst();
    }

}
