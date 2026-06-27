package com.ecommerce.adapter.security.jwt;

import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.auth.exception.InvalidTokenException;
import com.ecommerce.domain.auth.exception.TokenExpiredException;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.vo.Email;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    public IssuedToken createAccessToken(Email email, String deviceId, Instant now) {
        return createAccessToken(email, List.of(Role.USER), deviceId, now);
    }

    public IssuedToken createAccessToken(Email email, List<Role> roles, String deviceId, Instant now) {

        long accessTokenMilliSeconds = accessTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(Date.from(now).getTime() + accessTokenMilliSeconds);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email.address())
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
    public IssuedToken createRefreshToken(Email email, List<Role> roles, String deviceId, Instant now) {
        long refreshTokenMilliSeconds = refreshTokenSeconds * ONE_SECOND_IN_MILLI;
        Date expiry = new Date(Date.from(now).getTime() + refreshTokenMilliSeconds);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email.address())
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

    public IssuedToken createRefreshToken(Email email, String deviceId, Instant now) {
        return createRefreshToken(email, List.of(Role.USER), deviceId, now);
    }

    public IssuedToken parseToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            List<String> roleNames = claims.get(ROLES, List.class);
            return IssuedToken.builder()
                    .email(new Email(claims.getSubject()))
                    .tokenType(TokenType.valueOf(claims.get(TOKEN_TYPE, String.class)))
                    .deviceId(claims.get(DEVICE_ID, String.class))
                    .roles(roleNames.stream().map(Role::valueOf).toList())
                    .token(token)
                    .expiresAt(claims.getExpiration().toInstant())
                    .build();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e.getMessage());
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
