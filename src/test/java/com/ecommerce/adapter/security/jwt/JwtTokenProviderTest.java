package com.ecommerce.adapter.security.jwt;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.enums.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String secret = "test-secret-key-that-is-at-least-32bytes-long!!";

    @Test
    @DisplayName("엑세스 토큰을 생성한다.")
    void createAccessToken() {
        // given
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);
        final Long userId = 1L;
        final String DEVICE_ID = "device-1";
        final Instant now = Instant.now();

        //when
        IssuedToken accessToken = tokenProvider.createAccessToken(userId, DEVICE_ID, now);

        assertThat(accessToken).isNotNull();
        assertThat(accessToken.token()).isNotNull();
        assertThat(accessToken.tokenType()).isEqualTo(TokenType.ACCESS);
        assertThat(accessToken.id()).isEqualTo(userId);


    }

    @Test
    void createRefreshToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);

        final Long userId = 1L;
        final String DEVICE_ID = "device-1";
        final Instant now = Instant.now();

        //when
        IssuedToken refreshToken = tokenProvider.createRefreshToken(userId, DEVICE_ID, now);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.token()).isNotNull();
        assertThat(refreshToken.tokenType()).isEqualTo(TokenType.REFRESH);
        assertThat(refreshToken.id()).isEqualTo(userId);


    }
}