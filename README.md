# 🚀 Cryptocurrency Algorithm Trading Platform

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📖 Overview

A **production-ready** Java cryptocurrency trading platform built with **Layered Architecture** principles. This project provides type-safe, high-performance API clients for multiple cryptocurrency exchanges with comprehensive error handling, authentication, and monitoring capabilities.

### ✨ Key Features

- 🏗️ **Layered Architecture**: Clean separation of concerns with infrastructure, service, and application layers
- 🔒 **Multi-Exchange Support**: Coinone and Binance APIs with unified interfaces
- 🛡️ **Type-Safe Operations**: Comprehensive DTO validation and error handling
- 🔐 **Secure Authentication**: Automatic request signing with HMAC-SHA algorithms
- 📊 **Comprehensive Logging**: Request/response tracking with structured logging
- ⚡ **Declarative HTTP Clients**: Interface-based HTTP clients using Spring 6 HTTP Interface
- 🧪 **Test Coverage**: Extensive unit and integration tests
- 📚 **Rich Documentation**: Complete JavaDoc and API documentation

## 🏛️ Architecture

This project follows **Layered Architecture** principles for maintainability and scalability:

```text
📁 Application Layer
├── 🎯 Service Layer (Business Logic)
└── 🏗️ Infrastructure Layer
    ├── 🌐 Client Layer (External APIs)
    ├── ⚙️ Configuration Layer
    ├── 🚨 Exception Handling
    └── 🔌 Interceptors & Middleware
```

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Language** | Java | 21 LTS |
| **Framework** | Spring Boot | 3.x |
| **Build Tool** | Gradle | 8.x |
| **Web Framework** | Spring Web | MVC |
| **Template Engine** | Thymeleaf | 3.x |
| **HTTP Client** | HTTP Interface | Declarative |
| **Real-time** | WebSocket + STOMP | Native |
| **Testing** | JUnit 5 + AssertJ | Latest |
| **Documentation** | JavaDoc | Built-in |

## 🚀 Quick Start

### Prerequisites

```bash
java --version    # Java 21+
gradle --version  # Gradle 8.x+
```

### 1. Clone & Setup

```bash
git clone https://github.com/sungjun1116/coin-trading.git
cd coin-trading
```

### 2. Environment Configuration

Create your environment configuration:

```yaml
# application.yml
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

### 3. Build & Run

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Start the application
./gradlew bootRun
```

## 📂 Project Structure

```text
🏗️ Layered Architecture Structure

src/main/java/sungjun/bitcoin/algorithmtrading/
├── 🎯 service/                           # 🎯 Service Layer
│   └── CoinoneService.java              # Business logic & orchestration
│
├── 🏗️ infrastructure/                   # 🏗️ Infrastructure Layer
│   ├── 🌐 client/                       # External API clients
│   │   ├── coinone/                     # Coinone exchange integration
│   │   │   ├── CoinoneAccountApiClient.java
│   │   │   ├── CoinoneOrderApiClient.java
│   │   │   ├── CoinoneTickerApiClient.java
│   │   │   ├── request/                 # Request DTOs
│   │   │   └── response/                # Response DTOs
│   │   └── binance/                     # Binance exchange integration
│   │       ├── BinanceAccountApiClient.java
│   │       ├── request/                 # Request DTOs
│   │       └── response/                # Response DTOs
│   │
│   ├── ⚙️ config/                       # Configuration layer
│   │   ├── coinone/                     # Coinone-specific config
│   │   │   ├── CoinoneProperties.java
│   │   │   ├── CoinoneRestClientConfig.java
│   │   │   └── CoinoneResponseErrorHandler.java
│   │   └── binance/                     # Binance-specific config
│   │       ├── BinanceProperties.java
│   │       ├── BinanceRestClientConfig.java
│   │       └── BinanceResponseErrorHandler.java
│   │
│   ├── 🚨 exception/                    # Exception handling
│   │   ├── coinone/CoinoneApiException.java
│   │   └── binance/BinanceApiException.java
│   │
│   └── 🔌 interceptor/                  # Middleware & interceptors
│       ├── common/LoggingInterceptor.java
│       ├── coinone/CoinoneAuthenticationInterceptor.java
│       └── binance/BinanceAuthenticationInterceptor.java
│
└── 🔧 util/                             # Utility classes
    └── SignatureUtils.java              # Cryptographic utilities

src/test/java/                           # 🧪 Test Layer
├── api/service/                         # Service layer tests
└── infrastructure/                      # Infrastructure tests
    ├── client/                          # API client tests
    └── config/                          # Configuration tests
```

## 💡 Usage Examples

### 🏪 Service Layer Usage (Recommended)

```java
@Autowired
private CoinoneService coinoneService;

// Get ticker information through service layer
public void getMarketData() {
    CoinoneTicker ticker = coinoneService.getTicker("KRW", "BTC");
    System.out.println("BTC/KRW Price: " + ticker.getLast());
}
```

### 🌐 Declarative HTTP Interface Clients

This project implements declarative HTTP clients using **Spring 6 HTTP Interface**:

#### Coinone API (HTTP Interface)

```java
@Component
@HttpExchange
public interface CoinoneAccountApiClient {
    
    @PostExchange("/account/balance")
    CoinoneAccountApiResponse getAccount(@RequestBody CoinoneAccountRequest requestBody);
    
    @PostExchange("/account/balance/all")
    CoinoneAccountApiResponse getAccounts(@RequestBody CoinoneAccountRequest requestBody);
}

// Usage
@Autowired
private CoinoneAccountApiClient coinoneAccountApiClient;

public void getAccountInfo() {
    CoinoneAccountRequest request = CoinoneAccountRequest.builder()
            .accessToken("your-access-token")
            .nonce(UUID.randomUUID().toString())
            .build();
    
    CoinoneAccountApiResponse response = coinoneAccountApiClient.getAccount(request);
    response.getBalances().forEach(balance -> 
        log.info("Currency: {}, Available: {}", 
                balance.getCurrency(), balance.getAvailable())
    );
}
```

#### Binance API (HTTP Interface)

```java
@Component
@HttpExchange
public interface BinanceAccountApiClient {
    
    @GetExchange("/api/v3/account")
    BinanceAccountApiResponse getAccount(
        @RequestParam(value = "omitZeroBalances", required = false) boolean omitZeroBalances,
        @RequestParam(value = "recvWindow", required = false) long recvWindow,
        @RequestParam("timestamp") long timestamp
    );
    
    // Default methods for convenience
    default BinanceAccountApiResponse getAccount(long timestamp) {
        return getAccount(false, 5000L, timestamp);
    }
}

// Usage
@Autowired
private BinanceAccountApiClient binanceAccountApiClient;

public void getAccountInfo() {
    long timestamp = System.currentTimeMillis();
    BinanceAccountApiResponse response = binanceAccountApiClient.getAccount(timestamp);
    
    response.getBalances().forEach(balance -> 
        log.info("Asset: {}, Free: {}, Locked: {}", 
                balance.getAsset(), balance.getFree(), balance.getLocked())
    );
}
```

### 🔧 HTTP Interface Benefits

- **Declarative Approach**: Automatic implementation generation from interface definitions
- **Type Safety**: Compile-time type verification
- **Boilerplate Reduction**: Minimal repetitive HTTP client code
- **WebClient Based**: High performance guaranteed through internal WebClient usage
- **Spring Integration**: Perfect integration with Spring ecosystem

## 🔐 Security Features

### Authentication Methods

| Exchange | Algorithm | Header Format |
|----------|-----------|---------------|
| **Coinone** | HMAC-SHA512 | `X-COINONE-PAYLOAD`, `X-COINONE-SIGNATURE` |
| **Binance** | HMAC-SHA256 | `X-MBX-APIKEY`, Query Parameter Signature |

### Error Handling

```java
try {
    CoinoneAccountApiResponse response = coinoneAccountApiClient.getAccount(request);
    // Process successful response
} catch (CoinoneApiException e) {
    log.error("Coinone API Error - Code: {}, Message: {}", 
              e.getErrorCode(), e.getMessage());
} catch (BinanceApiException e) {
    log.error("Binance API Error - Code: {}, Message: {}", 
              e.getErrorCode(), e.getErrorMessage());
}
```

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run integration tests
./gradlew integrationTest

# Generate test report
./gradlew jacocoTestReport
```

### Test Structure

- **Unit Tests**: Individual component testing
- **Integration Tests**: API client integration testing
- **Service Tests**: Business logic testing

## 📊 Monitoring & Logging

The application provides comprehensive logging for:

- 📨 **Request/Response Tracking**: All API calls logged with correlation IDs
- 🚨 **Error Monitoring**: Structured exception logging
- ⏱️ **Performance Metrics**: Request duration and success rates
- 🔒 **Security Events**: Authentication and authorization tracking

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/#{Issue ID}amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/#{Issue ID}amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines

- Follow **Clean Code** principles
- Maintain **Layered Architecture** separation
- Add comprehensive **JavaDoc** documentation
- Include **unit and integration tests**
- Follow existing **code style** conventions

## 📚 API Documentation

### Exchange API References

- 🟡 [Coinone API Documentation](https://docs.coinone.co.kr/)
- 🟠 [Binance API Documentation](https://developers.binance.com/docs/binance-spot-api-docs)

### Error Code References

- 📄 [Coinone Error Codes](docs/coinone-error-codes.md)
- 📄 [Binance Error Codes](docs/binance-error-codes.md)

## 🐛 Known Issues & Limitations

- Rate limiting not implemented (planned for v2.0)
- WebSocket streaming not supported (planned for v2.0)
- Limited to spot trading (futures trading planned)

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Cryptocurrency exchange APIs for public access
- Open source community for inspiration and tools

---

<div align="center">

**Made with ❤️ by [sungjun1116](https://github.com/sungjun1116)**

*If this project helps you, please consider giving it a ⭐!*

</div>
