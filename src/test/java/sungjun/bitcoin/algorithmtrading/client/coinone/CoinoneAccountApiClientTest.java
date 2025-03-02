package sungjun.bitcoin.algorithmtrading.client.coinone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import sungjun.bitcoin.algorithmtrading.client.coinone.request.CoinoneAccountRequest;
import sungjun.bitcoin.algorithmtrading.client.coinone.response.CoinoneAccountApiResponse;
import sungjun.bitcoin.algorithmtrading.config.CoinoneProperties;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EnableConfigurationProperties(CoinoneProperties.class)
@SpringBootTest
class CoinoneAccountApiClientTest {

    @Autowired
    CoinoneAccountApiClient coinoneAccountApiClient;

    @Autowired
    CoinoneProperties coinoneProperties;

    @DisplayName("전체 자산 잔고를 조회한다.")
    @Test
    void getAccounts() {
        // given
        String nonce = UUID.randomUUID().toString();
        String accessToken = coinoneProperties.getAccessToken();

        CoinoneAccountRequest payload = CoinoneAccountRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .build();

        // when
        CoinoneAccountApiResponse response = coinoneAccountApiClient.getAccounts(payload);

        // then
        assertThat(response).extracting("result").isEqualTo("success");
    }

    @DisplayName("특정 자산 잔고를 조회한다.")
    @Test
    void getAccount() {
        // given
        String nonce = UUID.randomUUID().toString();
        String accessToken = coinoneProperties.getAccessToken();
        List<String> currencies = List.of("BTC");

        CoinoneAccountRequest payload = CoinoneAccountRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .currencies(currencies)
            .build();

        // when
        CoinoneAccountApiResponse response = coinoneAccountApiClient.getAccount(payload);

        // then
        assertThat(response).extracting("result").isEqualTo("success");
    }

}
