package sungjun.bitcoin.algorithmtrading.infrastructure.client.coinone;

/**
 * Coinone 거래소에서 사용되는 주문 방향(매수/매도)을 나타내는 열거형입니다.
 * <p>
 * 주문 생성 시 매수 또는 매도 지정에 사용됩니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
public enum OrderSide {

    /**
     * 매수 주문
     */
    BUY,
    
    /**
     * 매도 주문
     */
    SELL;
}
