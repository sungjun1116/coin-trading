package sungjun.bitcoin.algorithmtrading.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import sungjun.bitcoin.algorithmtrading.client.coinone.request.CoinoneOrderCancleRequest;
import sungjun.bitcoin.algorithmtrading.client.coinone.request.CoinoneOrderRequest;
import sungjun.bitcoin.algorithmtrading.client.coinone.response.CoinoneOrderApiResponse;

import java.util.Map;

@Component
@HttpExchange
public interface CoinoneOrderApiClient {

    @PostExchange("/order")
    CoinoneOrderApiResponse order(@RequestBody CoinoneOrderRequest request);

    @PostExchange("/order/cancel/all")
    Map<String, String> cancelAll(@RequestBody CoinoneOrderCancleRequest request);

}
