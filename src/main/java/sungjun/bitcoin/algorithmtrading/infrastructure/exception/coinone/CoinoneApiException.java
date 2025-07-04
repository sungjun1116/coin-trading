package sungjun.bitcoin.algorithmtrading.infrastructure.exception.coinone;

import lombok.Getter;

/**
 * 코인원 API 호출 중 발생한 예외를 나타내는 클래스
 * 
 * <p>코인원 API에서 반환하는 에러 코드와 메시지를 포함합니다.</p>
 * 
 * <h2>에러 코드 범위</h2>
 * <ul>
 *   <li>100 ~ 199: 인증 관련 오류</li>
 *   <li>200 ~ 299: 요청 형식 오류</li>
 *   <li>300 ~ 399: 주문 관련 오류</li>
 *   <li>400 ~ 499: 계정 관련 오류</li>
 *   <li>500 ~ 599: 서버 관련 오류</li>
 * </ul>
 * 
 * <h2>자주 발생하는 에러</h2>
 * <ul>
 *   <li>100: API 키 인증 실패</li>
 *   <li>101: 잘못된 API 서명</li>
 *   <li>104: 허용되지 않은 IP 주소</li>
 *   <li>200: 잘못된 파라미터</li>
 *   <li>303: 충분하지 않은 잔고</li>
 *   <li>502: 과도한 요청으로 인한 제한</li>
 * </ul>
 * 
 * <p>더 자세한 에러 코드 정보는 <a href="https://docs.coinone.co.kr/">코인원 API 공식 문서</a>를 참조하거나,
 * 프로젝트 내 {@code docs/coinone-error-codes.md} 문서를 확인하세요.</p>
 * 
 * @see sungjun.bitcoin.algorithmtrading.infrastructure.config.coinone.CoinoneResponseErrorHandler 코인원 API 응답 에러 처리기
 * @see <a href="https://docs.coinone.co.kr/">코인원 API 문서</a>
 * @see <a href="file:///docs/coinone-error-codes.md">프로젝트 에러 코드 가이드</a>
 */
@Getter
public class CoinoneApiException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public CoinoneApiException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
