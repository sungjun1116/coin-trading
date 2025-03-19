package sungjun.bitcoin.algorithmtrading.client.coinone.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import sungjun.bitcoin.algorithmtrading.client.coinone.config.CoinoneProperties;
import sungjun.bitcoin.algorithmtrading.util.SignatureUtils;

import java.io.IOException;
import java.util.Base64;

import static sungjun.bitcoin.algorithmtrading.client.coinone.config.CoinoneProperties.X_COINONE_PAYLOD;
import static sungjun.bitcoin.algorithmtrading.client.coinone.config.CoinoneProperties.X_COINONE_SIGNATURE;

@Component
@RequiredArgsConstructor
public class CoinoneAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final CoinoneProperties properties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String encodedPayload = Base64.getEncoder().encodeToString(body);
        String signature = SignatureUtils.makeSignature(properties.getSecretKey(), encodedPayload);
        request.getHeaders().add(X_COINONE_PAYLOD, encodedPayload);
        request.getHeaders().add(X_COINONE_SIGNATURE, signature);

        return execution.execute(request, body);
    }
}
