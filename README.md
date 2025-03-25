# Cryptocurrency Exchange API Client

## Overview
This project is a Java client library that provides easy access to multiple cryptocurrency exchange APIs (Coinone, Binance). Built on Spring Boot, it offers object-oriented interfaces for account information retrieval, order execution, market data queries, and other various exchange API functionalities.

## Key Features
- Integrated support for multiple cryptocurrency exchanges (currently Coinone and Binance)
- Type-safe API interfaces
- Automatic request signing and authentication
- DTO-based response objects for type-safe data handling
- Request/response logging
- Error handling

## Technology Stack
- Java 21
- Spring Boot
- Gradle

## Getting Started

### Dependencies
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    // Other necessary dependencies...
}
```

### Configuration
Set up your API information in the `application.yml` file:

```yaml
coinone:
  api:
    public-url: https://api.coinone.co.kr/public/v2
    private-url: https://api.coinone.co.kr/v2.1/
    access-token: ${COINONE_ACCESS_TOKEN}
    secret-key: ${COINONE_SECRET_KEY}
    connection-timeout: 3000
    read-timeout: 5000
    signature-algorithm: HmacSHA512

binance:
  api:
    url: https://api.binance.com
    api-key: ${BINANCE_API_KEY}
    secret-key: ${BINANCE_SECRET_KEY}
    connection-timeout: 3000
    read-timeout: 5000
    signature-algorithm: HmacSHA256
```

### Signature Algorithms
Different exchanges use different signature algorithms for their API authentication:

- **Coinone**: Uses HMAC-SHA512 for signature generation
- **Binance**: Uses HMAC-SHA256 for signature generation

The signature algorithm can be configured in the application.yml file, and the appropriate interceptor will handle the signing process automatically.

## Usage Examples

### Coinone API Examples

#### Retrieve Account Information
```java
@Autowired
private CoinoneAccountApiClient coinoneAccountApiClient;

public void getAccountInfo() {
    CoinoneAccountRequest request = CoinoneAccountRequest.builder()
            .accessToken("your-access-token")
            .build();
    
    CoinoneAccountApiResponse response = coinoneAccountApiClient.getAccount(request);
    List<CoinoneBalance> balances = response.getBalances();
    
    // Utilize account balance information
    balances.forEach(balance -> {
        System.out.println("Currency: " + balance.getCurrency() + 
                          ", Available: " + balance.getAvailable());
    });
}
```

#### Get Ticker Information
```java
@Autowired
private CoinoneTickerApiClient coinoneTickerApiClient;

public void getTickerInfo() {
    CoinoneTickerApiResponse response = coinoneTickerApiClient.getTicker("KRW", "BTC");
    System.out.println("BTC Price: " + response.getLastPrice());
}
```

### Binance API Examples

#### Retrieve Account Information
```java
@Autowired
private BinanceAccountApiClient binanceAccountApiClient;

public void getAccountInfo() {
    long timestamp = System.currentTimeMillis();
    BinanceAccountApiResponse response = binanceAccountApiClient.getAccount(timestamp);
    
    // Utilize account balance information
    response.getBalances().forEach(balance -> {
        System.out.println("Asset: " + balance.getAsset() + 
                          ", Free: " + balance.getFree() + 
                          ", Locked: " + balance.getLocked());
    });
}
```

## Project Structure
```
└── src/
    ├── main/
    │   ├── java/sungjun/bitcoin/algorithmtrading/
    │   │   ├── client/                            # API clients
    │   │   │   ├── coinone/                       # Coinone API clients
    │   │   │   │   ├── CoinoneAccountApiClient.java
    │   │   │   │   ├── CoinoneOrderApiClient.java
    │   │   │   │   ├── CoinoneTickerApiClient.java
    │   │   │   │   ├── request/                  # Request DTOs
    │   │   │   │   ├── response/                 # Response DTOs
    │   │   │   │   ├── interceptor/              # Authentication & logging
    │   │   │   │   └── config/                   # Configuration
    │   │   │   ├── binance/                       # Binance API clients
    │   │   │   │   ├── BinanceAccountApiClient.java
    │   │   │   │   ├── request/                  # Request DTOs
    │   │   │   │   ├── response/                 # Response DTOs
    │   │   │   │   ├── interceptor/              # Authentication & logging
    │   │   │   │   └── config/                   # Configuration
    │   │   │   └── interceptor/                   # Common interceptors
    │   │   ├── api/                               # API services
    │   │   └── util/                              # Utility classes
    │   └── resources/
    │       └── application.yml                    # Application configuration
    └── test/
        └── java/sungjun/bitcoin/algorithmtrading/ # Test code
```

## Reference Documentation
- [Coinone API Documentation](https://docs.coinone.co.kr/)
- [Binance API Documentation](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/account-endpoints)

## Contributing
Please contribute to the project via issues or pull requests.

## License
This project is licensed under the MIT License.
