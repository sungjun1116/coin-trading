package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response.CoinoneTickerApiResponse;

@Component
@HttpExchange
public interface CoinoneTickerApiClient {

    @GetExchange("/ticker_new/{quoteCurrency}/{targetCurrency}")
    CoinoneTickerApiResponse getTicker(@PathVariable String quoteCurrency, @PathVariable String targetCurrency);

}
