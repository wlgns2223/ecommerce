package com.ecommerce.adapter.security.jwt;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.vo.Email;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String secret = "test-secret-key-that-is-at-least-32bytes-long!!";

    @Test
    @DisplayName("엑세스 토큰을 생성한다.")
    void createAccessToken() {
        // given
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);
        final Email email = new Email("test@gmail.com");
        final String DEVICE_ID = "device-1";
        final Instant now = Instant.now();

        //when
        IssuedToken accessToken = tokenProvider.createAccessToken(email, DEVICE_ID, now);

        assertThat(accessToken).isNotNull();
        assertThat(accessToken.token()).isNotNull();
        assertThat(accessToken.tokenType()).isEqualTo(TokenType.ACCESS);
        assertThat(accessToken.email()).isEqualTo(email.address());


    }

    @Test
    void createRefreshToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);

        final Email email = new Email("test@gmail.com");
        final String DEVICE_ID = "device-1";
        final Instant now = Instant.now();

        //when
        IssuedToken refreshToken = tokenProvider.createRefreshToken(email, DEVICE_ID, now);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.token()).isNotNull();
        assertThat(refreshToken.tokenType()).isEqualTo(TokenType.REFRESH);
        assertThat(refreshToken.email()).isEqualTo(email.address());


    }
}