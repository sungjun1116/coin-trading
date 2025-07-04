package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

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
@ConfigurationProperties(prefix = "binance.api")
public class BinanceProperties {

    /**
     * Binance API 기본 URL
     */
    private String url;

    /**
     * Binance API 키 (Access Token)
     */
    private String accessToken;

    /**
     * Binance API 비밀 키 (서명 생성용)
     */
    private String secretKey;

    /**
     * HTTP 연결 타임아웃 (초 단위)
     */
    private Long connectionTimeout;

    /**
     * HTTP 읽기 타임아웃 (초 단위)
     */
    private Long readTimeout;

    /**
     * 서명 생성에 사용할 알고리즘 (예: HmacSHA256)
     */
    private String signatureAlgorithm;

    /**
     * BinanceProperties 생성자입니다.
     *
     * @param url API 기본 URL
     * @param accessToken API 키
     * @param secretKey API 비밀 키
     * @param connectionTimeout 연결 타임아웃
     * @param readTimeout 읽기 타임아웃
     * @param signatureAlgorithm 서명 알고리즘
     */
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
