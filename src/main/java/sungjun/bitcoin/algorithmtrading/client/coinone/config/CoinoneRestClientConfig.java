package sungjun.bitcoin.algorithmtrading.client.coinone.config;

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
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneAccountApiClient;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneOrderApiClient;
import sungjun.bitcoin.algorithmtrading.client.coinone.CoinoneTickerApiClient;
import sungjun.bitcoin.algorithmtrading.client.coinone.interceptor.CoinoneAuthenticationInterceptor;
import sungjun.bitcoin.algorithmtrading.client.interceptor.LoggingInterceptor;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
