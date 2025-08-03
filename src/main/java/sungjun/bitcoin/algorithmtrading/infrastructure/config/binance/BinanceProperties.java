package sungjun.bitcoin.algorithmtrading.infrastructure.config.binance;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Binance API 설정 프로퍼티 클래스입니다.
 * <p>
 * Binance API 호출에 필요한 설정 값들을 외부 설정 파일에서 로드합니다.
 * {@code binance.api} 로 시작하는 설정 값들을 바인딩합니다.
 * </p>
 *
 * <h2>설정 예시</h2>
 * <pre>
 * binance:
 *   api:
 *     url: https://api.binance.com
 *     access-token: your-api-key
 *     secret-key: your-secret-key
 *     connection-timeout: 10
 *     read-timeout: 30
 *     signature-algorithm: HmacSHA256
 * </pre>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@Validated
@ToString(exclude = {"accessToken", "secretKey"})
@ConfigurationProperties(prefix = "binance.api")
public class BinanceProperties {

    /**
     * Binance API 기본 URL
     */
    @NotBlank(message = "Binance API URL은 필수 값입니다.")
    private String url;

    /**
     * Binance API 키 (Access Token)
     */
    @NotBlank(message = "Access token은 필수 값입니다.")
    private String accessToken;

    /**
     * Binance API 비밀 키 (서명 생성용)
     */
    @NotBlank(message = "Secret key는 필수 값입니다.")
    private String secretKey;

    /**
     * HTTP 연결 타임아웃 (초 단위)
     */
    @NotNull(message = "연결 타임아웃은 필수 값입니다.")
    private Integer connectionTimeout;

    /**
     * HTTP 읽기 타임아웃 (초 단위)
     */
    @NotNull(message = "읽기 타임아웃은 필수 값입니다.")
    private Integer readTimeout;

    /**
     * 서명 생성에 사용할 알고리즘 (예: HmacSHA256)
     */
    @NotBlank(message = "서명 알고리즘은 필수 값입니다.")
    private String signatureAlgorithm;

    /**
     * Constructs a BinanceProperties instance with the specified API configuration values.
     *
     * @param url the base URL for the Binance API
     * @param accessToken the API key used for authentication
     * @param secretKey the secret key used for signature generation
     * @param connectionTimeout the HTTP connection timeout in seconds
     * @param readTimeout the HTTP read timeout in seconds
     * @param signatureAlgorithm the algorithm used for signature generation (e.g., HmacSHA256)
     */
    @ConstructorBinding
    public BinanceProperties(String url, String accessToken, String secretKey, Integer connectionTimeout, Integer readTimeout, String signatureAlgorithm) {
        this.url = url;
        this.accessToken = accessToken;
        this.secretKey = secretKey;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.signatureAlgorithm = signatureAlgorithm;
    }
}
