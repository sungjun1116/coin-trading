package sungjun.bitcoin.algorithmtrading.client.binance.config;

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
import sungjun.bitcoin.algorithmtrading.client.binance.BinanceAccountApiClient;
import sungjun.bitcoin.algorithmtrading.client.binance.interceptor.BinanceAuthenticationInterceptor;
import sungjun.bitcoin.algorithmtrading.client.interceptor.LoggingInterceptor;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(BinanceProperties.class)
public class BinanceRestClientConfig {

    private final BinanceProperties properties;
    private final BinanceAuthenticationInterceptor authenticationInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    @Bean
    public BinanceAccountApiClient binanceAccountApiClient(RestClient.Builder builder) {
        RestClient restClient = builder
            .baseUrl(properties.getUrl())
            .requestFactory(createClientHttpRequestFactory())
            .defaultHeaders(this::setDefaultHeaders)
            .requestInterceptors(interceptors -> {
                interceptors.add(authenticationInterceptor);
                interceptors.add(loggingInterceptor);
            })
            .build();

        return createHttpServiceProxy(restClient, BinanceAccountApiClient.class);
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        ClientHttpRequestFactorySettings factorySettings = ClientHttpRequestFactorySettings
            .defaults()
            .withConnectTimeout(Duration.ofSeconds(properties.getConnectionTimeout()))
            .withReadTimeout(Duration.ofSeconds(properties.getReadTimeout()));

        ClientHttpRequestFactory factory = ClientHttpRequestFactoryBuilder.detect().build(factorySettings);
        return new BufferingClientHttpRequestFactory(new BinanceSignedClientHttpRequestFactory(properties, factory));
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
