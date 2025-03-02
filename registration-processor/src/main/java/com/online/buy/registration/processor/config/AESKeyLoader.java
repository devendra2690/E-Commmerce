package com.online.buy.registration.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESKeyLoader {

    @Value("${AES_SECRET_KEY}")
    private String base64Key;

    @Bean
    public SecretKeySpec getSecretKey() {
        if (base64Key == null || base64Key.isEmpty()) {
            throw new IllegalStateException("AES_SECRET_KEY is not set!");
        }
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
