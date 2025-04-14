package com.example.absoluteweb.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Whirlpool2PasswordEncoder implements PasswordEncoder {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = digest.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hashBytes));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Помилка при хешуванні пароля", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
