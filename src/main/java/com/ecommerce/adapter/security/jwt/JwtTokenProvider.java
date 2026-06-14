package com.ecommerce.adapter.security.jwt;

import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider implements TokenProvider {

    public static final String TOKEN_TYPE = "type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";

    private static final Long ONE_SECOND_IN_MILLI = 1000L;

    private final SecretKey key;
    private final Long accessTokenSeconds;
    private final Long refreshTokenSeconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String key,
                            @Value("${jwt.access-token-seconds}") Long accessTokenSeconds,
                            @Value("${jwt.refresh-token-seconds}") Long refreshTokenSeconds) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.accessTokenSeconds = accessTokenSeconds;
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    public String createAccessToken(String email, List<Role> roles, Date now) {

        long accessTokenMilliSeconds = accessTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(now.getTime() + accessTokenMilliSeconds);

        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE, ACCESS_TOKEN_TYPE)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String email, Date now) {

        long refreshTokenMilliSeconds = refreshTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(now.getTime() + refreshTokenMilliSeconds);

        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE, REFRESH_TOKEN_TYPE)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims parseClaim(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean validate(String token) {
        try {
            parseClaim(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        return validate(token) && ACCESS_TOKEN_TYPE.equals(parseClaim(token).get(TOKEN_TYPE));
    }
}
