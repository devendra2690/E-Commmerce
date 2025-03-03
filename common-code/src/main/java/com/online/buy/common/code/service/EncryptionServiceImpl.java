package com.online.buy.common.code.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Getter
@Slf4j
@Service
public class EncryptionServiceImpl {

    private final SecretKey secretKey;

    public EncryptionServiceImpl() {
        this.secretKey = loadSecretKey();
    }

    private SecretKey loadSecretKey() {
        String encodedKey = System.getenv("ENCRYPTION_SECRET_KEY");
        log.info(" Loaded Secret key from Enn : {} ",encodedKey);
        if (encodedKey == null || encodedKey.isEmpty()) {
            throw new IllegalStateException("Secret key not set in environment variables!");
        }

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
    }

   /*

   Use this code to generate secret key and register it in env variable

   public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Key: " + encodedKey);
    }*/
}
