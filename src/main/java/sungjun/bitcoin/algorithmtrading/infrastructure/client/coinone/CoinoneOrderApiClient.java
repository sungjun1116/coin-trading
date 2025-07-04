package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneOrderCancleRequest;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneOrderRequest;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneOrderApiResponse;

import java.util.Map;

/**
 * Coinone 거래소의 주문 관리 API 클라이언트입니다.
 * <p>
 * 이 클라이언트는 Coinone API의 order 엔드포인트와 통신하여
 * 매수/매도 주문 생성, 주문 취소 등의 거래 기능을 제공합니다.
 * 모든 API 호출은 인증이 필요합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Component
@HttpExchange
public interface CoinoneOrderApiClient {

    /**
     * 새로운 주문을 생성합니다.
     * <p>
     * 지정된 통화쌍에 대해 매수 또는 매도 주문을 생성합니다.
     * 주문 유형(지정가/시장가)과 수량, 가격 등을 지정할 수 있습니다.
     * </p>
     *
     * @param request 주문 생성 요청 데이터
     * @return {@link CoinoneOrderApiResponse} 주문 결과 응답
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.exception.CoinoneApiException API 호출 실패 시
     */
    @PostExchange("/order")
    CoinoneOrderApiResponse order(@RequestBody CoinoneOrderRequest request);

    /**
     * 모든 대기 주문을 일괄 취소합니다.
     * <p>
     * 사용자의 모든 대기 주문(미체결 주문)을 일괄적으로 취소합니다.
     * 주의: 이 작업은 되돌릴 수 없습니다.
     * </p>
     *
     * @param request 주문 취소 요청 데이터
     * @return {@code Map<String, String>} 취소 결과 정보
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.exception.CoinoneApiException API 호출 실패 시
     */
    @PostExchange("/order/cancel/all")
    Map<String, String> cancelAll(@RequestBody CoinoneOrderCancleRequest request);

}
