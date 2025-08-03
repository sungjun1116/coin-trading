package sungjun.bitcoin.algorithmtrading.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 거래소별 시세 정보를 표준화된 형태로 제공하는 DTO 클래스입니다.
 * <p>
 * 다양한 거래소의 시세 정보를 통합하여 일관된 형태로 제공합니다.
 * 심볼은 API 호출 시점에 지정되며, 단일 가격 정보를 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@Getter
@Builder
public class TickerDto {
    
    /**
     * 거래소 명 (예: "coinone", "binance")
     */
    private final String exchange;

    /**
     * 거래쌍 심볼 (예: "BTCUSDT", "BTC/KRW")
     */
    private final String symbol;

    /**
     * 현재 가격
     */
    private final BigDecimal price;
    
    /**
     * 24시간 고가
     */
    private final BigDecimal highPrice;
    
    /**
     * 24시간 저가
     */
    private final BigDecimal lowPrice;
    
    /**
     * 매수 호가
     */
    private final BigDecimal bidPrice;
    
    /**
     * 매도 호가
     */
    private final BigDecimal askPrice;

    /**
     * 24시간 변동률 (%)
     */
    private final BigDecimal changePercent;
    
    /**
     * 24시간 거래량
     */
    private final BigDecimal volume;
    
    /**
     * 데이터 갱신 시각
     */
    private final LocalDateTime timestamp;
}
