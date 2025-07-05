package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request.CoinoneOrderCancelRequest;
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
     * Creates a new buy or sell order for a specified currency pair on the Coinone exchange.
     * <p>
     * Allows specifying order type (limit or market), quantity, price, and other order details.
     * </p>
     *
     * @param request the order creation request containing order parameters
     * @return the API response representing the result of the order creation
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    @PostExchange("/order")
    CoinoneOrderApiResponse order(@RequestBody CoinoneOrderRequest request);

    /**
     * Cancels all pending (unfilled) orders for the user on the Coinone exchange.
     * <p>
     * This operation is irreversible and will remove all open orders associated with the user's account.
     * </p>
     *
     * @param request the cancellation request containing relevant user and order information
     * @return a map containing details about the cancellation results
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    @PostExchange("/order/cancel/all")
    Map<String, String> cancelAll(@RequestBody CoinoneOrderCancelRequest request);

}
