package sungjun.bitcoin.algorithmtrading.client;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import sungjun.bitcoin.algorithmtrading.api.service.ClientTestConfiguration;

@Import(ClientTestConfiguration.class)
@SpringBootTest
public abstract class IntegrationClientTestSupport {
}
