# Coinone Cryptocurrency Trading API Client for Java

A Spring Boot-based Java client library that provides seamless integration with the Coinone cryptocurrency exchange API, enabling automated trading with support for market data retrieval, account management, and order execution.

This project implements a comprehensive API client for the Coinone cryptocurrency exchange, offering robust functionality for both public and private API endpoints. It features type-safe API interfaces, automatic request signing, comprehensive error handling, and detailed request/response logging. The library is built with Spring Boot and follows modern Java practices, making it ideal for building automated trading systems or integrating Coinone trading capabilities into existing applications.

## Repository Structure
```
├── src/
│   ├── main/java/sungjun/bitcoin/algorithmtrading/
│   │   ├── AlgorithmTradingApplication.java    # Main application entry point
│   │   ├── api/service/
│   │   │   └── CoinoneService.java            # High-level service for Coinone operations
│   │   ├── client/coinone/                    # API client implementations
│   │   │   ├── CoinoneAccountApiClient.java   # Account operations client
│   │   │   ├── CoinoneOrderApiClient.java     # Order management client
│   │   │   └── CoinoneTickerApiClient.java    # Market data client
│   │   └── config/                            # Configuration classes
│   │       ├── CoinoneProperties.java         # API configuration properties
│   │       ├── RestClientConfig.java          # REST client configuration
│   │       └── interceptor/                   # HTTP client interceptors
│   └── resources/
│       └── application.yml                    # Application configuration
└── build.gradle                               # Gradle build configuration
```

## Usage Instructions
### Prerequisites
- Java 21 or higher
- Gradle 7.x or higher
- Coinone API credentials (access token and secret key)

### Installation
1. Add the dependency to your `build.gradle`:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

2. Configure Coinone API credentials in `application.yml`:
```yaml
coinone.api:
  public-url: https://api.coinone.co.kr/public/v2
  private-url: https://api.coinone.co.kr/v2.1/
  access-token: ${coinone-access-token}
  secret-key: ${coinone-secret-key}
  connection-timeout: 3000
  read-timeout: 5000
```

### Quick Start
1. Initialize the Coinone service:
```java
@Autowired
private CoinoneService coinoneService;
```

2. Fetch market data:
```java
CoinoneTiker ticker = coinoneService.getTicker("KRW", "BTC");
```

### More Detailed Examples
1. Place a market sell order:
```java
CoinoneOrderRequest request = CoinoneOrderRequest.createMarketSellOrder(
    accessToken,
    UUID.randomUUID().toString(),
    OrderSide.SELL,
    "KRW",
    "BTC",
    "0.00005",
    null
);
CoinoneOrderApiResponse response = coinoneOrderApiClient.order(request);
```

2. Place a limit buy order:
```java
CoinoneOrderRequest request = CoinoneOrderRequest.createLimitOrder(
    accessToken,
    UUID.randomUUID().toString(),
    OrderSide.BUY,
    "KRW",
    "BTC",
    "50000000",
    "0.001",
    true
);
CoinoneOrderApiResponse response = coinoneOrderApiClient.order(request);
```

### Troubleshooting
1. Authentication Issues
- Problem: API requests failing with authentication errors
- Solution: Verify your access token and secret key in application.yml
- Debug: Enable logging interceptor to view request/response details
```java
logging.level.sungjun.bitcoin.algorithmtrading=DEBUG
```

2. Request Timeout Issues
- Problem: API requests timing out
- Solution: Adjust timeout settings in application.yml
```yaml
coinone.api:
  connection-timeout: 5000
  read-timeout: 8000
```

## Data Flow
The library handles API communication with Coinone exchange through a layered architecture.

```ascii
[Client Application] -> [CoinoneService] -> [API Clients] -> [HTTP Interceptors] -> [Coinone API]
     |                       |                  |                    |                    |
     |                       |                  |                    |                    |
     v                       v                  v                    v                    v
Configuration         Business Logic     API Interfaces     Authentication/Logging    Exchange
```

Key component interactions:
1. CoinoneService provides high-level trading operations
2. API Clients handle specific endpoint communications
3. Authentication Interceptor automatically signs requests
4. Logging Interceptor provides request/response debugging
5. Error Handler processes API-specific error responses
6. Properties configuration manages API credentials and settings
7. RestClient manages HTTP communication and retry logic