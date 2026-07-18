package com.ecommerce.application.auth.provided;

import com.ecommerce.adapter.security.jwt.JwtTokenProvider;
import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.auth.exception.InvalidTokenException;
import com.ecommerce.domain.auth.exception.TokenExpiredException;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Profile("test")
@Transactional
class AuthenticatorTest {

    @Autowired
    Authenticator authenticator;

    @Autowired
    com.ecommerce.application.user.provided.UserRegister userRegister;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("이메일 패스워드를 통해 로그인하면 JWT토큰을 발급받는다.")
    void login() {
        // given
        String email = "test@gmail.com";
        UserRegister registerRequest = UserFixture.createUserRegister(email);
        userRegister.register(registerRequest);
        UserLoginRequest request = UserFixture.createLoginRequest(email);

        // when
        TokenResult result = authenticator.login(request);

        // then
        assertThat(result.access()).isNotNull();
        assertThat(result.refresh()).isNotNull();

    }

    @Test
    @DisplayName("만료된 refresh token을 받으면 에러를 던진다.")
    void renewAccessToken() {
        // given
        IssuedToken issuedToken = jwtTokenProvider.createRefreshToken(
                1L,
                "device-1",
                Instant.now().minus(Duration.ofDays(30))
        );

        // then
        assertThatThrownBy(() -> authenticator.renew(issuedToken.token())).isInstanceOf(TokenExpiredException.class);
    }

    @Test
    @DisplayName("잘못된 토큰이면 에러를 던진다.")
    void renewThrowExceptionForInvalidToken() {
        // given
        String invalidToken = "invalid-token";

        // then
        assertThatThrownBy(() -> authenticator.renew(invalidToken)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("만료되지 않은 정상 토큰이면 새 Token Result(access, refresh)를 리턴한다.")
    void renew() {
        // given
        String email = "test@gmail.com";
        UserRegister registerRequest = UserFixture.createUserRegister(email);
        User user = userRegister.register(registerRequest);
        UserLoginRequest loginRequest = UserFixture.createLoginRequest(email);
        TokenResult loginResult = authenticator.login(loginRequest);

        // when
        TokenResult result = authenticator.renew(loginResult.refresh().token());

        // then
        assertThat(result).isNotNull();
        assertThat(result.access()).isNotNull();
        assertThat(result.refresh()).isNotNull();

        IssuedToken issuedAccessToken = jwtTokenProvider.parseToken(result.access().token());
        IssuedToken issuedRefreshToken = jwtTokenProvider.parseToken(result.refresh().token());
        assertThat(issuedAccessToken.id()).isEqualTo(user.getId());
        assertThat(issuedRefreshToken.id()).isEqualTo(user.getId());

    }

}