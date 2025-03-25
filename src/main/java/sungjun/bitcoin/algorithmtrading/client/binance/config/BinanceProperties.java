package sungjun.bitcoin.algorithmtrading.client.binance.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "binance.api")
public class BinanceProperties {

    private String url;

    private String accessToken;

    private String secretKey;

    private Long connectionTimeout;

    private Long readTimeout;

    private String signatureAlgorithm;

    @ConstructorBinding
    public BinanceProperties(String url, String accessToken, String secretKey, Long connectionTimeout, Long readTimeout, String signatureAlgorithm) {
        this.url = url;
        this.accessToken = accessToken;
        this.secretKey = secretKey;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.signatureAlgorithm = signatureAlgorithm;
    }
}
