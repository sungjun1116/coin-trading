package sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneAccountApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneOrderApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.infrastructure.interceptor.coinone.CoinoneAuthenticationInterceptor;
import sungjun.bitcoin.algorithmtrading.infrastructure.interceptor.common.LoggingInterceptor;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Coinone API 클라이언트 설정 클래스입니다.
 * <p>
 * Coinone 거래소와의 통신을 위한 RestClient 및 HTTP Service Proxy를 설정합니다.
 * 공개 API와 비공개 API를 위한 별도의 클라이언트를 생성합니다.
 * </p>
 *
 * <h2>주요 기능</h2>
 * <ul>
 *   <li>Ticker API 클라이언트 (공개 API)</li>
 *   <li>Account API 클라이언트 (비공개 API, 인증 필요)</li>
 *   <li>Order API 클라이언트 (비공개 API, 인증 필요)</li>
 * </ul>
 *
 * @author sungjun
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(CoinoneProperties.class)
public class CoinoneRestClientConfig {

    private final CoinoneProperties properties;
    private final CoinoneAuthenticationInterceptor authenticationInterceptor;
    private final LoggingInterceptor loggingInterceptor;
    private final CoinoneResponseErrorHandler responseErrorHandler;

    @Bean
    public CoinoneTickerApiClient coinoneTickerApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(properties.getPublicUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> interceptors.add(loggingInterceptor))
            .defaultStatusHandler(responseErrorHandler)
            .build();

        return createHttpServiceProxy(restClient, CoinoneTickerApiClient.class);
    }

    @Bean
    public CoinoneAccountApiClient coinoneAccountApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(properties.getPrivateUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> {
                interceptors.add(authenticationInterceptor);
                interceptors.add(loggingInterceptor);
            })
            .defaultStatusHandler(responseErrorHandler)
            .build();

        return createHttpServiceProxy(restClient, CoinoneAccountApiClient.class);
    }

    @Bean
    public CoinoneOrderApiClient coinoneOrderApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(properties.getPrivateUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> {
                interceptors.add(authenticationInterceptor);
                interceptors.add(loggingInterceptor);
            })
            .defaultStatusHandler(responseErrorHandler)
            .build();

        return createHttpServiceProxy(restClient, CoinoneOrderApiClient.class);
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        ClientHttpRequestFactorySettings factorySettings = ClientHttpRequestFactorySettings
            .defaults()
            .withConnectTimeout(Duration.ofSeconds(properties.getConnectionTimeout()))
            .withReadTimeout(Duration.ofSeconds(properties.getReadTimeout()));

        ClientHttpRequestFactory factory = ClientHttpRequestFactoryBuilder.detect().build(factorySettings);
        return new BufferingClientHttpRequestFactory(factory);
    }

    private void setDefaultHeaders(HttpHeaders headers) {
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }

    private <T> T createHttpServiceProxy(RestClient restClient, Class<T> clientClass) {
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build()
            .createClient(clientClass);
    }

}
