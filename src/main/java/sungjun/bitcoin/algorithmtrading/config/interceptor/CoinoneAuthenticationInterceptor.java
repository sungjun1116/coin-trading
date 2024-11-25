package sungjun.bitcoin.algorithmtrading.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import sungjun.bitcoin.algorithmtrading.util.SignatureUtils;

import java.io.IOException;
import java.util.Base64;

import static sungjun.bitcoin.algorithmtrading.config.CoinoneProperties.X_COINONE_PAYLOD;
import static sungjun.bitcoin.algorithmtrading.config.CoinoneProperties.X_COINONE_SIGNATURE;

@Slf4j
public class CoinoneAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private String secretKey;

    public CoinoneAuthenticationInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String encodedPayload = Base64.getEncoder().encodeToString(body);
        String signature = SignatureUtils.makeSignature(secretKey, encodedPayload);
        request.getHeaders().add(X_COINONE_PAYLOD, encodedPayload);
        request.getHeaders().add(X_COINONE_SIGNATURE, signature);

        return execution.execute(request, body);
    }
}
