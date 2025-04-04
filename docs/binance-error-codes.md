# Binance API 에러 코드 가이드

Binance API 호출 시 발생할 수 있는 주요 에러 코드와 그 의미에 대한 가이드입니다.

## 에러 코드 범위

Binance API 에러 코드는 다음과 같은 범위로 구분됩니다:

- `-1000 ~ -1099`: 서버 관련 오류
- `-1100 ~ -1199`: 요청 형식 오류
- `-1200 ~ -1299`: 서비스 또는 계정 문제
- `-2000 ~ -2099`: 필터 관련 오류

## 주요 에러 코드 및 설명

### 서버 관련 오류
- `-1000`: 알 수 없는 오류
- `-1001`: 내부 서버 오류
- `-1002`: 권한 없음
- `-1003`: 너무 많은 요청 (Rate limit 초과)
- `-1006`: 비정상적인 서버 유지보수
- `-1007`: 서버 유지보수 중
- `-1010`: IP 차단됨
- `-1021`: 타임스탬프가 서버 시간과 1000ms 이상 차이남
- `-1022`: 서명이 올바르지 않음

### 요청 형식 오류
- `-1100`: 잘못된 요청 파라미터
- `-1101`: 실행 파라미터가 너무 많음
- `-1102`: 필수 파라미터 누락
- `-1103`: 알 수 없는 파라미터
- `-1104`: 존재하지 않는 잔고 또는 거래 내역
- `-1121`: 잘못된 심볼

### 계정 및 필터 관련 오류
- `-2010`: 충분하지 않은 잔고
- `-2011`: 알 수 없는 계정 상태
- `-2013`: 주문 실패
- `-2014`: 주문을 찾을 수 없음
- `-2015`: 요청 비중 초과
- `-2018`: 주문 수량이 최소 수량보다 작음

## 주문 관련 오류
- `-2010`: 충분하지 않은 잔고
- `-2011`: 알 수 없는 계정 상태
- `-2013`: 주문 실패
- `-2014`: 주문을 찾을 수 없음
- `-2015`: 요청 비중 초과
- `-2018`: 주문 수량이 최소 수량보다 작음
- `-2019`: 주문 수량이 정밀도 요구사항을 충족하지 않음
- `-2020`: 주문 가격이 정밀도 요구사항을 충족하지 않음

## WAF(Web Application Firewall) 관련 오류
- `-1010`: IP 차단됨
- `-1015`: WAF 제한 초과 (너무 많은 요청)

## 에러 처리 방법

Binance API 에러가 발생했을 때 다음과 같은 방법으로 처리할 수 있습니다:

1. **Rate limit 관련 오류** (`-1003`, `-2015`): 
   - 요청 주기를 줄이거나 잠시 후 재시도합니다.
   - 헤더의 `X-MBX-USED-WEIGHT` 값을 모니터링하여 API 사용량을 관리합니다.

2. **인증 관련 오류** (`-1002`, `-1022`): 
   - API 키와 시크릿이 올바른지 확인합니다.
   - 서명 생성 방식이 정확한지 확인합니다.
   - 타임스탬프가 서버 시간과 동기화되어 있는지 확인합니다.

3. **파라미터 오류** (`-1100` ~ `-1103`): 
   - 요청 파라미터가 API 문서에 명시된 형식과 제약조건을 따르는지 검증합니다.
   - 필수 파라미터가 모두 포함되어 있는지 확인합니다.

4. **심볼 오류** (`-1121`): 
   - 올바른 심볼을 사용했는지 확인합니다.
   - `/api/v3/exchangeInfo` 엔드포인트를 통해 유효한 심볼 목록을 확인할 수 있습니다.

5. **잔고 부족** (`-2010`): 
   - 거래 전 충분한 잔고가 있는지 확인합니다.
   - 주문 수량과 수수료를 고려한 필요 잔고를 계산합니다.

6. **주문 정밀도 오류** (`-2018`, `-2019`, `-2020`):
   - `/api/v3/exchangeInfo` 엔드포인트에서 각 심볼의 필터 정보를 확인하여 정밀도와 최소/최대 수량 요구사항을 확인합니다.

## 예제 코드

### 1. Rate Limit 초과 에러(-1003) 처리

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import sungjun.bitcoin.algorithmtrading.client.binance.exception.BinanceApiException;

@Slf4j
public class BinanceApiService {

    /**
     * 재시도 로직이 구현된 API 호출 메서드
     * Rate limit 초과(-1003) 에러 발생 시 최대 3회 재시도 (지수 백오프 방식)
     */
    @Retryable(
        value = BinanceApiException.class, 
        maxAttempts = 3, 
        backoff = @Backoff(delay = 1000, multiplier = 2),
        include = RateLimitExceededPredicate.class
    )
    public AccountInfoResponse getAccountInfo() {
        try {
            log.info("계정 정보 요청 시도...");
            return binanceAccountApiClient.getAccountInfo();
        } catch (BinanceApiException e) {
            log.error("Binance API 에러: [{}] {}", e.getErrorCode(), e.getErrorMessage());
            throw e;
        }
    }
    
    /**
     * Rate limit 초과 에러인지 확인하는 조건 클래스
     */
    public static class RateLimitExceededPredicate implements Predicate<BinanceApiException> {
        @Override
        public boolean test(BinanceApiException e) {
            return "-1003".equals(e.getErrorCode());
        }
    }
}
```

### 2. 타임스탬프 오류(-1021) 처리

```java
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import sungjun.bitcoin.algorithmtrading.client.binance.exception.BinanceApiException;

@Slf4j
public class BinanceTimeService {

    /**
     * 서버 시간 동기화 후 API 호출
     */
    public void executeWithSyncedTime() {
        try {
            // API 호출
            marketDataResponse = binanceMarketApiClient.getMarketData();
        } catch (BinanceApiException e) {
            if ("-1021".equals(e.getErrorCode())) {
                log.warn("타임스탬프 오류 발생, 서버 시간 동기화 후 재시도");
                
                // 서버 시간 조회 및 동기화
                long serverTime = binanceMarketApiClient.getServerTime().getServerTime();
                long localTime = Instant.now().toEpochMilli();
                long timeDiff = serverTime - localTime;
                
                log.info("서버 시간: {}, 로컬 시간: {}, 차이: {}ms", serverTime, localTime, timeDiff);
                
                // 시간 차이를 보정하여 재시도
                TimeManager.setTimeOffset(timeDiff);
                
                // 재시도
                marketDataResponse = binanceMarketApiClient.getMarketData();
            } else {
                throw e;
            }
        }
    }
}
```

### 3. 주문 파라미터 오류(-1100, -2018, -2019, -2020) 처리

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import sungjun.bitcoin.algorithmtrading.client.binance.exception.BinanceApiException;

@Slf4j
public class BinanceOrderService {

    /**
     * 주문 생성 전 파라미터 검증 및 필터 적용
     */
    public OrderResponse createOrder(String symbol, String side, String type, BigDecimal quantity, BigDecimal price) {
        try {
            // 심볼 정보 조회
            SymbolInfo symbolInfo = binanceMarketApiClient.getExchangeInfo()
                .getSymbols().stream()
                .filter(s -> s.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid symbol: " + symbol));
            
            // 수량 필터 적용
            BigDecimal minQty = new BigDecimal(symbolInfo.getFilters().getLotSizeFilter().getMinQty());
            BigDecimal maxQty = new BigDecimal(symbolInfo.getFilters().getLotSizeFilter().getMaxQty());
            int quantityPrecision = getDecimalPlaces(symbolInfo.getFilters().getLotSizeFilter().getStepSize());
            
            // 가격 필터 적용
            BigDecimal minPrice = new BigDecimal(symbolInfo.getFilters().getPriceFilter().getMinPrice());
            BigDecimal maxPrice = new BigDecimal(symbolInfo.getFilters().getPriceFilter().getMaxPrice());
            int pricePrecision = getDecimalPlaces(symbolInfo.getFilters().getPriceFilter().getTickSize());
            
            // 수량 검증 및 보정
            if (quantity.compareTo(minQty) < 0) {
                log.warn("주문 수량({}), 최소 수량({})보다 작음", quantity, minQty);
                throw new IllegalArgumentException("Order quantity is less than minimum");
            }
            if (quantity.compareTo(maxQty) > 0) {
                log.warn("주문 수량({}), 최대 수량({})보다 큼", quantity, maxQty);
                throw new IllegalArgumentException("Order quantity is greater than maximum");
            }
            
            // 수량 정밀도 보정
            BigDecimal adjustedQuantity = quantity.setScale(quantityPrecision, RoundingMode.DOWN);
            
            // 가격 정밀도 보정
            BigDecimal adjustedPrice = price.setScale(pricePrecision, RoundingMode.DOWN);
            
            log.info("조정된 주문: 수량={}, 가격={}", adjustedQuantity, adjustedPrice);
            
            // 주문 생성
            return binanceOrderApiClient.createOrder(
                symbol, side, type, adjustedQuantity.toPlainString(), adjustedPrice.toPlainString());
                
        } catch (BinanceApiException e) {
            log.error("주문 생성 실패: [{}] {}", e.getErrorCode(), e.getErrorMessage());
            throw e;
        }
    }
    
    /**
     * 소수점 자릿수 계산
     */
    private int getDecimalPlaces(String stepSize) {
        BigDecimal bd = new BigDecimal(stepSize);
        return Math.max(0, bd.stripTrailingZeros().scale());
    }
}
```

## 참고 자료

자세한 에러 코드 및 설명은 [Binance API 공식 문서](https://developers.binance.com/docs/binance-spot-api-docs/errors)를 참조하세요.

## IDE 연동

이 문서의 에러 코드는 Java 소스 코드의 JavaDoc에도 포함되어 있으므로, IDE에서 `BinanceApiException` 클래스를 살펴보면 주요 에러 코드와 처리 방법에 대한 정보를 얻을 수 있습니다.

```java
/**
 * 에러 코드 확인 예제:
 * if (e.getErrorCode().equals("-1003")) {
 *     // Rate limit 초과 처리
 * }
 */
```