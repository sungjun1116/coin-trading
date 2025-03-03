package sungjun.bitcoin.algorithmtrading.config;

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
import sungjun.bitcoin.algorithmtrading.config.interceptor.CoinoneAuthenticationInterceptor;
import sungjun.bitcoin.algorithmtrading.config.interceptor.LoggingInterceptor;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(CoinoneProperties.class)
public class RestClientConfig {

    private final CoinoneProperties coinoneProperties;

    private final CoinoneAuthenticationInterceptor coinoneAuthenticationInterceptor;

    private final LoggingInterceptor loggingInterceptor;

    @Bean
    public CoinoneTickerApiClient coinoneTickerApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(coinoneProperties.getPublicUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> interceptors.add(loggingInterceptor))
            .defaultStatusHandler(new CoinoneResponseErrorHandler())
            .build();

        return createHttpServiceProxy(restClient, CoinoneTickerApiClient.class);
    }

    @Bean
    public CoinoneAccountApiClient coinoneAccountApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(coinoneProperties.getPrivateUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> {
                interceptors.add(coinoneAuthenticationInterceptor);
                interceptors.add(loggingInterceptor);
            })
            .defaultStatusHandler(new CoinoneResponseErrorHandler())
            .build();

        return createHttpServiceProxy(restClient, CoinoneAccountApiClient.class);
    }

    @Bean
    public CoinoneOrderApiClient coinoneOrderApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(coinoneProperties.getPrivateUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> {
                interceptors.add(coinoneAuthenticationInterceptor);
                interceptors.add(loggingInterceptor);
            })
            .defaultStatusHandler(new CoinoneResponseErrorHandler())
            .build();

        return createHttpServiceProxy(restClient, CoinoneOrderApiClient.class);
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        ClientHttpRequestFactorySettings factorySettings = ClientHttpRequestFactorySettings
            .defaults()
            .withConnectTimeout(Duration.ofSeconds(coinoneProperties.getConnectionTimeout()))
            .withReadTimeout(Duration.ofSeconds(coinoneProperties.getReadTimeout()));

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
