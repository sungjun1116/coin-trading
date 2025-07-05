package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

/**
 * Coinone 비공개 API 요청의 공통 기본 클래스입니다.
 * <p>
 * 인증이 필요한 모든 Coinone API 요청에 공통으로 포함되는
 * 액세스 토큰과 nonce 값을 정의합니다.
 * JSON 직렬화 시 null 값은 제외하고 snake_case 형태로 변환됩니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class CoinonePrivateBaseRequest {

    /**
     * Coinone API 액세스 토큰
     */
    protected String accessToken;

    /**
     * 요청 고유 번호 (리플레이 공격 방지용)
     */
    protected String nonce;

    /**
     * Initializes a new authenticated Coinone API request with the specified access token and nonce.
     *
     * @param accessToken the API access token used for authentication
     * @param nonce a unique identifier for the request to prevent replay attacks
     */
    protected CoinonePrivateBaseRequest(String accessToken, String nonce) {
        this.accessToken = accessToken;
        this.nonce = nonce;
    }
}
