package sungjun.bitcoin.algorithmtrading.client.coinone.exception;

public class CoinoneApiException extends RuntimeException {

    private String errorCode;

    public CoinoneApiException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

}

