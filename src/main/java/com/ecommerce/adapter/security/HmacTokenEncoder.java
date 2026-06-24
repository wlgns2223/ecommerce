package com.ecommerce.adapter.security;

import com.ecommerce.domain.auth.required.TokenHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class HmacTokenEncoder implements TokenHasher {

    private static final String ALGO = "HmacSHA256";
    private final SecretKeySpec keySpec;

    public HmacTokenEncoder(@Value("${spring.jwt.secret}") String secret) {
        this.keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGO);
    }

    @Override
    public String hash(String raw) {
        try{
            Mac mac = Mac.getInstance(ALGO);
            mac.init(keySpec);
            byte[] digest = mac.doFinal(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest); // 64 length hex
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException("HMAC 초기화 실패");
        }
    }

    @Override
    public boolean matches(String rawToken, String hashedToken) {
        return MessageDigest.isEqual(hash(rawToken).getBytes(StandardCharsets.UTF_8), hashedToken.getBytes(StandardCharsets.UTF_8));
    }
}
