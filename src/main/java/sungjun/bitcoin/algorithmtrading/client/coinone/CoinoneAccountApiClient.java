package sungjun.bitcoin.algorithmtrading.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import sungjun.bitcoin.algorithmtrading.client.coinone.request.CoinoneAccountRequest;
import sungjun.bitcoin.algorithmtrading.client.coinone.response.CoinoneAccountApiResponse;

@Component
@HttpExchange
public interface CoinoneAccountApiClient {

    @PostExchange("/account/balance")
    CoinoneAccountApiResponse getAccount(@RequestBody CoinoneAccountRequest requestBody);

    @PostExchange("/account/balance/all")
    CoinoneAccountApiResponse getAccounts(@RequestBody CoinoneAccountRequest requestBody);

}
