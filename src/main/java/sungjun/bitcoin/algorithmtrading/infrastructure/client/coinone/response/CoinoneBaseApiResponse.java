package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class CoinoneBaseApiResponse {

    protected String result;

    protected String errorCode;

    protected CoinoneBaseApiResponse(String result, String errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }
}
