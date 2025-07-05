package sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Coinone API 설정 프로퍼티 클래스입니다.
 * <p>
 * Coinone API 호출에 필요한 설정 값들을 외부 설정 파일에서 로드합니다.
 * {@code coinone.api} 로 시작하는 설정 값들을 바인딩합니다.
 * </p>
 *
 * <h2>설정 예시</h2>
 * <pre>
 * coinone:
 *   api:
 *     public-url: https://api.coinone.co.kr
 *     private-url: https://api.coinone.co.kr
 *     access-token: your-access-token
 *     secret-key: your-secret-key
 *     connection-timeout: 10
 *     read-timeout: 30
 *     signature-algorithm: HmacSHA512
 * </pre>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@Validated
@ConfigurationProperties(prefix = "coinone.api")
public class CoinoneProperties {

    /**
     * Coinone API 요청에 사용되는 Payload 헤더 이름입니다.
     */
    public static final String X_COINONE_PAYLOAD = "X-COINONE-PAYLOAD";
    
    /**
     * Coinone API 요청에 사용되는 Signature 헤더 이름입니다.
     */
    public static final String X_COINONE_SIGNATURE = "X-COINONE-SIGNATURE";

    /**
     * 공개 API URL (인증이 필요하지 않은 API용)
     */
    @NotBlank(message = "공개 API URL은 필수 값입니다.")
    private String publicUrl;

    /**
     * 비공개 API URL (인증이 필요한 API용)
     */
    @NotBlank(message = "비공개 API URL은 필수 값입니다.")
    private String privateUrl;

    /**
     * Coinone API 액세스 토큰
     */
    @NotBlank(message = "Access token은 필수 값입니다.")
    private String accessToken;

    /**
     * Coinone API 비밀 키 (서명 생성용)
     */
    @NotBlank(message = "Secret key는 필수 값입니다.")
    private String secretKey;

    /**
     * HTTP 연결 타임아웃 (초 단위)
     */
    @NotNull(message = "연결 타임아웃은 필수 값입니다.")
    private Long connectionTimeout;

    /**
     * HTTP 읽기 타임아웃 (초 단위)
     */
    @NotNull(message = "읽기 타임아웃은 필수 값입니다.")
    private Long readTimeout;

    /**
     * 서명 생성에 사용할 알고리즘 (예: HmacSHA512)
     */
    @NotBlank(message = "서명 알고리즘은 필수 값입니다.")
    private String signatureAlgorithm;

    /**
     * CoinoneProperties 생성자입니다.
     *
     * @param publicUrl 공개 API URL
     * @param privateUrl 비공개 API URL
     * @param accessToken API 액세스 토큰
     * @param secretKey API 비밀 키
     * @param connectionTimeout 연결 타임아웃
     * @param readTimeout 읽기 타임아웃
     * @param signatureAlgorithm 서명 알고리즘
     */
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
