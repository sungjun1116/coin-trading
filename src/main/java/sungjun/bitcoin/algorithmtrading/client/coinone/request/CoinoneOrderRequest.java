package sungjun.bitcoin.algorithmtrading.client.coinone.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import sungjun.bitcoin.algorithmtrading.client.coinone.OrderSide;
import sungjun.bitcoin.algorithmtrading.client.coinone.OrderType;

@Getter
public class CoinoneOrderRequest extends CoinonePrivateBaseRequest {

    private OrderSide side;
    private String quoteCurrency;
    private String targetCurrency;
    private OrderType type;
    private String price;
    private String qty;
    private String amount;
    private boolean postOnly;
    private String limitPrice;
    private String triggerPrice;

    @Builder(access = AccessLevel.PRIVATE)
    private CoinoneOrderRequest(String accessToken,
                                String nonce,
                                OrderSide side,
                                String quoteCurrency,
                                String targetCurrency,
                                OrderType type,
                                String price,
                                String qty,
                                String amount,
                                boolean postOnly,
                                String limitPrice,
                                String triggerPrice) {
        super(accessToken, nonce);
        this.side = side;
        this.quoteCurrency = quoteCurrency;
        this.targetCurrency = targetCurrency;
        this.type = type;
        this.price = price;
        this.qty = qty;
        this.amount = amount;
        this.postOnly = postOnly;
        this.limitPrice = limitPrice;
        this.triggerPrice = triggerPrice;
    }

    // 시장가 매도
    public static CoinoneOrderRequest createMarketSellOrder(String accessToken,
                                                            String nonce,
                                                            OrderSide side,
                                                            String quoteCurrency,
                                                            String targetCurrency,
                                                            String qty,
                                                            String limitPrice) {
        return CoinoneOrderRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .side(side)
            .quoteCurrency(quoteCurrency)
            .targetCurrency(targetCurrency)
            .type(OrderType.MARKET)
            .amount(qty)
            .limitPrice(limitPrice)
            .build();
    }

    // 시장가 매수
    public static CoinoneOrderRequest createMarketBuyOrder(String accessToken,
                                                            String nonce,
                                                            OrderSide side,
                                                            String quoteCurrency,
                                                            String targetCurrency,
                                                            String amount,
                                                            String limitPrice) {
        return CoinoneOrderRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .side(side)
            .quoteCurrency(quoteCurrency)
            .targetCurrency(targetCurrency)
            .type(OrderType.MARKET)
            .amount(amount)
            .limitPrice(limitPrice)
            .build();
    }

    // 지정가 매수/매도
    public static CoinoneOrderRequest createLimitOrder(String accessToken,
                                                       String nonce,
                                                       OrderSide side,
                                                       String quoteCurrency,
                                                       String targetCurrency,
                                                       String price,
                                                       String qty,
                                                       boolean postOnly) {
        return CoinoneOrderRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .side(side)
            .quoteCurrency(quoteCurrency)
            .targetCurrency(targetCurrency)
            .type(OrderType.LIMIT)
            .price(price)
            .qty(qty)
            .postOnly(postOnly)
            .build();
    }

    // 예약가 매수/매도
    public static CoinoneOrderRequest createStopLimitOrder(String accessToken,
                                                           String nonce,
                                                           OrderSide side,
                                                           String quoteCurrency,
                                                           String targetCurrency,
                                                           String price,
                                                           String qty,
                                                           String triggerPrice) {
        return CoinoneOrderRequest.builder()
            .accessToken(accessToken)
            .nonce(nonce)
            .side(side)
            .quoteCurrency(quoteCurrency)
            .targetCurrency(targetCurrency)
            .type(OrderType.STOP_LIMIT)
            .price(price)
            .qty(qty)
            .triggerPrice(triggerPrice)
            .build();
    }
}
