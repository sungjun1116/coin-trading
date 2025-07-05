package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

/**
 * Coinone 거래소에서 사용되는 주문 유형을 나타내는 열거형입니다.
 * <p>
 * 주문 생성 시 지정가, 시장가, 손절매 등의 주문 유형을 지정하는데 사용됩니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
public enum OrderType {

    /**
     * 지정가 주문 - 원하는 가격에 매수/매도 주문
     */
    LIMIT,
    
    /**
     * 시장가 주문 - 현재 시장 가격에 즉시 체결
     */
    MARKET,
    
    /**
     * 손절매 주문 - 지정한 가격에 도달 시 자동 주문 실행
     */
    STOP_LIMIT;
}
