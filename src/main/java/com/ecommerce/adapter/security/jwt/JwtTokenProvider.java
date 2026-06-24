package com.ecommerce.adapter.security.jwt;

import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.auth.required.IssuedToken;
import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider implements TokenProvider {

    public static final String TOKEN_TYPE = "type";
    private static final String ROLES = "roles";
    private static final String DEVICE_ID = "deviceId";

    private static final Long ONE_SECOND_IN_MILLI = 1000L;

    private final SecretKey key;
    private final Long accessTokenSeconds;
    private final Long refreshTokenSeconds;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String key,
                            @Value("${spring.jwt.access-token-seconds}") Long accessTokenSeconds,
                            @Value("${spring.jwt.refresh-token-seconds}") Long refreshTokenSeconds) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.accessTokenSeconds = accessTokenSeconds;
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

     public IssuedToken createAccessToken(String email,String deviceId,Instant now) {
         return createAccessToken(email, List.of(Role.USER),deviceId, now);
    }

    public IssuedToken createAccessToken(String email, List<Role> roles, String deviceId ,Instant now) {

        long accessTokenMilliSeconds = accessTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(Date.from(now).getTime() + accessTokenMilliSeconds);
        String token = Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE, TokenType.ACCESS.name())
                .claim(ROLES, roles)
                .claim(DEVICE_ID, deviceId)
                .issuedAt(Date.from(now))
                .expiration(expiry)
                .signWith(key)
                .compact();

        return IssuedToken.builder()
                .email(email)
                .roles(roles)
                .tokenType(TokenType.ACCESS)
                .deviceId(deviceId)
                .token(token)
                .expiresAt(expiry.toInstant())
                .build();
    }

    @Override
    public IssuedToken createRefreshToken(String email, List<Role> roles, String deviceId, Instant now) {
        long refreshTokenMilliSeconds = refreshTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(Date.from(now).getTime() + refreshTokenMilliSeconds);
        String token = Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE, TokenType.REFRESH.name())
                .claim(ROLES, roles)
                .claim(DEVICE_ID, deviceId)
                .issuedAt(Date.from(now))
                .expiration(expiry)
                .signWith(key)
                .compact();

        return IssuedToken.builder()
                .email(email)
                .token(token)
                .roles(roles)
                .deviceId(deviceId)
                .expiresAt(expiry.toInstant())
                .tokenType(TokenType.REFRESH)
                .build();
    }

    public IssuedToken createRefreshToken(String email, String deviceId, Instant now) {
        return createRefreshToken(email, List.of(Role.USER), deviceId, now);
    }

    public IssuedToken parseToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return IssuedToken.builder()
                .email(claims.getSubject())
                .tokenType(claims.get(TOKEN_TYPE,TokenType.class))
                .deviceId(claims.get(DEVICE_ID, String.class))
                .roles(claims.get(ROLES, List.class))
                .token(token)
                .expiresAt(claims.getExpiration().toInstant())
                .build();
    }

    public boolean validate(String token) {
        try {
            parseToken(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        return validate(token) && TokenType.ACCESS.equals(parseToken(token).tokenType());
    }
}
