package com.ecommerce.adapter.security.jwt;

import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
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



    }

    @Test
    void createRefreshToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(secret, 10L, 100L);


    }
}