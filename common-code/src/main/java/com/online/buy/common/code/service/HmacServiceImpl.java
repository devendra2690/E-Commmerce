package com.online.buy.common.code.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class HmacServiceImpl {
    private static final String SECRET_KEY = "your-secret-hmac-key";

    public String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"));
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }

    public boolean verify(String data, String signature) throws Exception {
        return sign(data).equals(signature);
    }
}
