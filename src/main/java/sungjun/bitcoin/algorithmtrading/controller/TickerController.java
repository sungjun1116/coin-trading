package sungjun.bitcoin.algorithmtrading.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sungjun.bitcoin.algorithmtrading.service.TickerService;
import sungjun.bitcoin.algorithmtrading.service.dto.TickerDto;

import java.util.List;

/**
 * 실시간 시세 정보를 제공하는 REST API 컨트롤러입니다.
 * <p>
 * 여러 거래소의 시세 정보를 조회할 수 있는 엔드포인트를 제공합니다.
 * </p>
 *
 * @author sungjun
 * @since 1.0
 */
@RestController
@RequestMapping("/api/ticker")
public class TickerController {

    private final TickerService tickerService;

    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    /**
     * 통합 시세 정보 조회 API입니다.
     * 
     * @param targetCurrency 대상 통화 (예: BTC, ETH) - popular=true일 때는 무시됨
     * @param symbol 통화 심볼 (USDT, KRW) - 선택사항, 기본값은 모든 심볼
     * @param exchange 특정 거래소 (coinone, binance) - 선택사항
     * @param popular 인기 암호화폐 조회 여부 (true/false) - 선택사항
     * @return 시세 정보 또는 시세 정보 리스트
     */
    @GetMapping
    public ResponseEntity<?> getTickers(
        @RequestParam(required = false) String targetCurrency,
        @RequestParam(required = false) String symbol,
        @RequestParam(required = false, defaultValue = "false") boolean popular
    ) {
        // 인기 암호화폐 조회
        if (popular) {
            List<TickerDto> tickers = tickerService.getPopularTickers();
            return ResponseEntity.ok(tickers);
        }
        
        // targetCurrency 파라미터 필수 체크 (popular=false일 때)
        if (targetCurrency == null || targetCurrency.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body("targetCurrency parameter is required when popular=false");
        }
        
        // symbol이 없으면 해당 통화의 모든 거래소 조회
        if (symbol == null || symbol.trim().isEmpty()) {
            List<TickerDto> tickers = tickerService.getAllTickers(targetCurrency);
            return ResponseEntity.ok(tickers);
        }
        
        // 특정 심볼 조회
        TickerDto ticker;
        switch (symbol.toUpperCase()) {
            case "KRW":
                ticker = tickerService.getCoinoneTicker(targetCurrency);
                break;
            case "USDT":
                ticker = tickerService.getBinanceTicker(targetCurrency);
                break;
            default:
                return ResponseEntity.badRequest()
                    .body("Unsupported symbol: " + symbol + ". Supported: KRW, USDT");
        }

        return ResponseEntity.ok(List.of(ticker));
    }

}
