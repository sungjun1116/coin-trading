package sungjun.bitcoin.algorithmtrading.client.coinone.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class CoinonePrivateBaseRequest {

    protected String accessToken;

    protected String nonce;

    protected CoinonePrivateBaseRequest(String accessToken, String nonce) {
        this.accessToken = accessToken;
        this.nonce = nonce;
    }
}
