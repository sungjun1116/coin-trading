package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "coinone.api")
public class CoinoneProperties {

    public static final String X_COINONE_PAYLOD = "X-COINONE-PAYLOAD";
    public static final String X_COINONE_SIGNATURE = "X-COINONE-SIGNATURE";

    private String publicUrl;

    private String privateUrl;

    private String accessToken;

    private String secretKey;

    private Long connectionTimeout;

    private Long readTimeout;

    private String signatureAlgorithm;

    @ConstructorBinding
    public CoinoneProperties(String publicUrl, String privateUrl, String accessToken, String secretKey, Long connectionTimeout, Long readTimeout, String signatureAlgorithm) {
        this.publicUrl = publicUrl;
        this.privateUrl = privateUrl;
        this.accessToken = accessToken;
        this.secretKey = secretKey;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.signatureAlgorithm = signatureAlgorithm;
    }
}
