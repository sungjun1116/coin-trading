package sungjun.bitcoin.algorithmtrading.api.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ClientTestConfiguration.class)
@SpringBootTest
public abstract class IntegrationServiceTestSupport {
}
