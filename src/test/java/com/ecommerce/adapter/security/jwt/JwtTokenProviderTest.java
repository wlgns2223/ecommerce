package com.ecommerce.adapter.security.jwt;

import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String secret = "test-secret-key-that-is-at-least-32bytes-long!!";

    @Test
    @DisplayName("토큰 생성 테스트")
    void createAccessToken() {
        // given
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);
        Date now = new Date();

        // when
        String accessToken = tokenProvider.createAccessToken("foo", List.of(Role.USER), now);

        // then
        assertThat(accessToken).isNotBlank();

        Claims claims = tokenProvider.parseClaim(accessToken);
        assertThat(claims.getSubject()).isEqualTo("foo");
        assertThat(claims.get(JwtTokenProvider.TOKEN_TYPE)).isEqualTo(JwtTokenProvider.ACCESS_TOKEN_TYPE);

    }

    @Test
    void createRefreshToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);
        Date now = new Date();

        String refreshToken = tokenProvider.createRefreshToken("foo", now);

        assertThat(refreshToken).isNotBlank();
        Claims claims = tokenProvider.parseClaim(refreshToken);
        assertThat(claims.get(JwtTokenProvider.TOKEN_TYPE)).isEqualTo(JwtTokenProvider.REFRESH_TOKEN_TYPE);

    }
}