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
     * Submits a buy or sell order for a specified currency pair on the Coinone exchange.
     *
     * @param request the order details including type, quantity, price, and other parameters
     * @return the response containing the result of the order submission
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API request fails
     */
    @PostExchange("/order")
    CoinoneOrderApiResponse order(@RequestBody CoinoneOrderRequest request);

    /**
     * Cancels all open orders for the user on the Coinone exchange.
     * <p>
     * This action removes all pending orders linked to the user's account and cannot be undone.
     * </p>
     *
     * @param request the cancellation request with user and order details
     * @return a map containing information about the cancellation outcome
     * @throws sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone.CoinoneApiException if the API call fails
     */
    @PostExchange("/order/cancel/all")
    Map<String, String> cancelAll(@RequestBody CoinoneOrderCancelRequest request);

}
