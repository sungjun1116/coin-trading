package sungjun.bitcoin.algorithmtrading.client.binance.exception;

import lombok.Getter;

/**
 * Binance API 호출 중 발생한 예외를 나타내는 클래스
 */
@Getter
public class BinanceApiException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public BinanceApiException(String errorCode, String errorMessage) {
        super(String.format("Binance API Error: [%s] %s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
