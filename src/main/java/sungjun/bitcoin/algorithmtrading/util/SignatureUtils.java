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
     * Generates an HMAC signature for the given string data using the specified algorithm and secret key.
     * <p>
     * The input string is encoded as UTF-8 before signature generation. The resulting signature is returned as a lowercase hexadecimal string.
     * </p>
     *
     * @param secretKey the secret key used for HMAC generation
     * @param data the input data to sign
     * @param algorithm the HMAC algorithm to use (e.g., HmacSHA256, HmacSHA512)
     * @return the generated HMAC signature as a hexadecimal string
     * @throws RuntimeException if signature generation fails
     */
    public static String makeSignature(String secretKey, String data, String algorithm) {
        return makeSignature(secretKey, data.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    /**
     * Generates an HMAC signature for the given byte array using the specified algorithm and secret key.
     *
     * Converts the resulting signature to a lowercase hexadecimal string. Throws a RuntimeException if the algorithm is unavailable or the key is invalid.
     *
     * @param secretKey the secret key used for HMAC generation
     * @param data the data to sign as a byte array
     * @param algorithm the HMAC algorithm to use (e.g., HmacSHA256, HmacSHA512)
     * @return the signature as a lowercase hexadecimal string
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
     * Converts a byte array to a lowercase hexadecimal string.
     *
     * Each byte is represented as a two-character hex string, with a leading zero if necessary.
     *
     * @param bytes the byte array to convert
     * @return a lowercase hexadecimal string representation of the byte array
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
