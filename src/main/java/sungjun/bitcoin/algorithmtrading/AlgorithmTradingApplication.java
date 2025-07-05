package sungjun.bitcoin.algorithmtrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 알고리즘 트레이딩 애플리케이션의 메인 클래스입니다.
 * <p>
 * 이 애플리케이션은 Spring Boot 기반으로 개발된 암호화폐 거래소 연동 서비스입니다.
 * Coinone, Binance 등의 거래소 API와 연동하여 시세 조회, 계정 관리, 주문 처리 등의
 * 기능을 제공합니다.
 * </p>
 *
 * <h2>주요 기능</h2>
 * <ul>
 *   <li>다중 거래소 API 연동 (Coinone, Binance)</li>
 *   <li>실시간 시세 정보 조회</li>
 *   <li>계정 잔고 및 자산 관리</li>
 *   <li>주문 생성 및 관리</li>
 *   <li>Layered Architecture 기반 체계적인 코드 구조</li>
 * </ul>
 *
 * @author sungjun
 * @since 1.0
 */
@SpringBootApplication
public class AlgorithmTradingApplication {

    /**
     * Application entry point that launches the Spring Boot algorithm trading service.
     *
     * Starts the application context and embedded server using the provided command-line arguments.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(AlgorithmTradingApplication.class, args);
    }

}
