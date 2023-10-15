package dev.wms.pwrapi.security.encryption;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SymmetricEncryptImpl implements SymmetricEncrypt {

    @Value("${encryption.algorithm-name}")
    private String ALGORITHM_NAME;

    @Value("${encryption.secret}")
    private String PASSWORD_SECRET;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    private void afterInit() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        // Check if algorithm is correct
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);

        this.secretKeySpec = new SecretKeySpec(
                PASSWORD_SECRET.getBytes(StandardCharsets.UTF_8),
                ALGORITHM_NAME
        );

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    }

    @Override
    @SneakyThrows
    public String encode(CharSequence rawPassword) {
        return encode(rawPassword.toString());
    }

    @Override
    public String encode(String rawPassword) {
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch(Exception e){
            return rawPassword;
        }
    }

    @Override
    @SneakyThrows
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return decode(encodedPassword).equals(rawPassword.toString());
    }

    @Override
    @SneakyThrows
    public String decode(String encodedPassword) {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encodedPassword));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
