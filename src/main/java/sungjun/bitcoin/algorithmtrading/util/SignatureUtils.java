package sungjun.bitcoin.algorithmtrading.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class SignatureUtils {

    public static String makeSignature(String secretKey, String data) {
        return makeSignature(secretKey, data.getBytes(StandardCharsets.UTF_8));
    }

    public static String makeSignature(String secretKey, byte[] data) {
        try {
            // Convert secret key to bytes using UTF-8
            byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            // Create MAC instance with HmacSHA512
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(keySpec);

            // Generate signature
            byte[] signature = mac.doFinal(data);

            // Convert to hexadecimal
            return bytesToHex(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

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
