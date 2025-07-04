package sungjun.bitcoin.algorithmtrading.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 암호화폐 거래소 API 인증에 사용되는 서명 생성 유틸리티 클래스입니다.
 * <p>
 * HMAC (Hash-based Message Authentication Code) 알고리즘을 사용하여
 * API 요청에 필요한 디지털 서명을 생성합니다.
 * </p>
 *
 * <h2>지원 알고리즘</h2>
 * <ul>
 *   <li>HmacSHA256 (Binance API)</li>
 *   <li>HmacSHA512 (Coinone API)</li>
 * </ul>
 *
 * @author sungjun
 * @since 1.0
 */
@UtilityClass
public class SignatureUtils {

    /**
     * 문자열 데이터로 HMAC 서명을 생성합니다.
     * <p>
     * 입력된 문자열을 UTF-8 인코딩으로 변환한 후 서명을 생성합니다.
     * </p>
     *
     * @param secretKey API 비밀 키
     * @param data 서명을 생성할 데이터 (문자열)
     * @param algorithm HMAC 알고리즘 (HmacSHA256, HmacSHA512 등)
     * @return 16진수 형태의 서명 문자열
     * @throws RuntimeException 서명 생성 실패 시
     */
    public static String makeSignature(String secretKey, String data, String algorithm) {
        return makeSignature(secretKey, data.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    /**
     * 바이트 배열 데이터로 HMAC 서명을 생성합니다.
     * <p>
     * 지정된 HMAC 알고리즘을 사용하여 디지털 서명을 생성하고
     * 16진수 문자열로 반환합니다.
     * </p>
     *
     * @param secretKey API 비밀 키
     * @param data 서명을 생성할 데이터 (바이트 배열)
     * @param algorithm HMAC 알고리즘 (HmacSHA256, HmacSHA512 등)
     * @return 16진수 형태의 서명 문자열
     * @throws RuntimeException 알고리즘을 찾을 수 없거나 키가 잘못된 경우
     */
    public static String makeSignature(String secretKey, byte[] data, String algorithm) {
        try {
            // Convert secret key to bytes using UTF-8
            byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            // Create MAC instance with specified algorithm
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, algorithm);
            mac.init(keySpec);

            // Generate signature
            byte[] signature = mac.doFinal(data);

            // Convert to hexadecimal
            return bytesToHex(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환합니다.
     * <p>
     * 각 바이트를 2자리 16진수 문자열로 변환하여 연결합니다.
     * 한 자리 숫자인 경우 앞에 0을 붙여 2자리로 만듭니다.
     * </p>
     *
     * @param bytes 변환할 바이트 배열
     * @return 16진수 문자열 (소문자)
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
