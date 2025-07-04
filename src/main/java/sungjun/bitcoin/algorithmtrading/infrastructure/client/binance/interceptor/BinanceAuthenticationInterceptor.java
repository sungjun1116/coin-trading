package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.config.BinanceProperties;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BinanceAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final BinanceProperties properties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // API 키 헤더만 추가하고 서명 로직은 제거
        request.getHeaders().add("X-MBX-APIKEY", properties.getAccessToken());
        
        // 서명 관련 로직을 제거하고 단순히 요청 실행
        return execution.execute(request, body);
    }
}
