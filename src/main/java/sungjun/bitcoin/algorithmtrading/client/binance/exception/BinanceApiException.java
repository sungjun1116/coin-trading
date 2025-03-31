package sungjun.bitcoin.algorithmtrading.client.binance.exception;

import lombok.Getter;

/**
 * Binance API 호출 중 발생한 예외를 나타내는 클래스
 * 
 * <p>Binance API에서 반환하는 에러 코드와 메시지를 포함합니다.</p>
 * 
 * <h2>에러 코드 범위</h2>
 * <ul>
 *   <li>-1000 ~ -1099: 서버 관련 오류</li>
 *   <li>-1100 ~ -1199: 요청 형식 오류</li>
 *   <li>-1200 ~ -1299: 서비스 또는 계정 문제</li>
 *   <li>-2000 ~ -2099: 필터 관련 오류</li>
 * </ul>
 * 
 * <h2>자주 발생하는 에러</h2>
 * <ul>
 *   <li>-1003: 너무 많은 요청 (Rate limit 초과)</li>
 *   <li>-1021: 타임스탬프가 서버 시간과 1000ms 이상 차이남</li>
 *   <li>-1022: 서명이 올바르지 않음</li>
 *   <li>-1100: 잘못된 요청 파라미터</li>
 *   <li>-1121: 잘못된 심볼</li>
 *   <li>-2010: 충분하지 않은 잔고</li>
 * </ul>
 * 
 * <p>더 자세한 에러 코드 정보는 <a href="https://developers.binance.com/docs/binance-spot-api-docs/errors">Binance API 공식 문서</a>를 참조하거나,
 * 프로젝트 내 {@code docs/binance-error-codes.md} 문서를 확인하세요.</p>
 * 
 * @see sungjun.bitcoin.algorithmtrading.client.binance.config.BinanceResponseErrorHandler 바이낸스 API 응답 에러 처리기
 * @see <a href="https://developers.binance.com/docs/binance-spot-api-docs/errors">Binance API Error Codes</a>
 * @see <a href="file:///docs/binance-error-codes.md">프로젝트 에러 코드 가이드</a>
 */
@Getter
public class BinanceApiException extends RuntimeException {
    private final int errorCode;
    private final String errorMessage;

    public BinanceApiException(int errorCode, String errorMessage) {
        super(String.format("Binance API Error: [%s] %s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
