package dev.wms.pwrapi.security.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface SymmetricEncrypt extends PasswordEncoder {

    String encode(String rawPassword);

    String decode(String encodedPassword);
}
