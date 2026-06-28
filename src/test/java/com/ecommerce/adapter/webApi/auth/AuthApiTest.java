package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.application.auth.provided.Authenticator;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
class AuthApiTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private Authenticator authenticator;

    @Test
    @DisplayName("register api")
    void register() throws JsonProcessingException {
        // given
        UserRegisterRequest request = UserFixture.createUserRegisterRequest("foo@email.com");
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        // then
        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.data.email",
                        (value) -> assertThat(value).isEqualTo(request.email()))
                .hasPathSatisfying("$.data.id", (value) -> assertThat(value).isNotNull());


    }

    @Test
    @DisplayName("로그인을 하면 access token, refresh token이 발급된다.")
    void login() throws JsonProcessingException {
        // given
        String email = "test@gmail.com";
        String password = "password";
        UserRegisterRequest registerRequest = UserFixture.createUserRegisterRequest(email, password);
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);

        mockMvcTester.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson)
                .exchange();

        UserLoginRequest request = UserFixture.createLoginRequest(email, password);
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();
        // then
        assertThat(result).hasStatusOk().cookies()
                .containsCookie("access_token")
                .containsCookie("refresh_token");

    }

    @Test
    @DisplayName("refresh token을 보내어 토큰을 갱신할 수 있다. 갱신시 access, refresh 모두 갱신됨")
    void renew() throws JsonProcessingException {
        // given
        String email = "test@gmail.com";
        String password = "password";

        // 회원 등록
        UserRegisterRequest registerRequest = UserFixture.createUserRegisterRequest(email, password);
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);
        mockMvcTester.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson)
                .exchange();

        // 로그인
        UserLoginRequest request = UserFixture.createLoginRequest(email, password);
        String requestJson = objectMapper.writeValueAsString(request);
        MvcTestResult loginResult = mockMvcTester.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        // 로그인으로 받은 토큰
        Optional<String> refreshToken = Arrays.stream(loginResult.getResponse().getCookies())
                .filter((c) -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
        assertThat(refreshToken).isNotEmpty();

        String oldRawRefreshToken = refreshToken.get();

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/auth/renew")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("refresh_token", oldRawRefreshToken))
                .exchange();

        Optional<String> newRefreshToken = Arrays.stream(result.getResponse().getCookies())
                .filter((c) -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();

        // then
        assertThat(result).hasStatusOk().cookies()
                .containsCookie("access_token")
                .containsCookie("refresh_token");
        assertThat(newRefreshToken).isNotEmpty();

        String newRawRefreshToken = newRefreshToken.get();

        assertThat(oldRawRefreshToken).isNotEqualTo(newRawRefreshToken);


    }

    @Test
    @DisplayName("로그아웃을 하면 쿠키를 삭제하고 refresh token을 revoke한다.")
    void logout() throws JsonProcessingException {
        // given
        String email = "test@gmail.com";
        String password = "password";

        // 회원 등록
        UserRegisterRequest registerRequest = UserFixture.createUserRegisterRequest(email, password);
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);
        mockMvcTester.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson)
                .exchange();

        // 로그인
        UserLoginRequest request = UserFixture.createLoginRequest(email, password);
        String requestJson = objectMapper.writeValueAsString(request);
        MvcTestResult loginResult = mockMvcTester.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        // 로그인으로 받은 토큰
        Optional<String> refreshToken = Arrays.stream(loginResult.getResponse().getCookies())
                .filter((c) -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
        assertThat(refreshToken).isNotEmpty();

        String oldRawRefreshToken = refreshToken.get();

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("refresh_token", oldRawRefreshToken))
                .exchange();
        // then
        assertThat(result).hasStatusOk()
                .cookies()
                .hasMaxAge("access_token", Duration.ZERO)
                .hasMaxAge("refresh_token", Duration.ZERO);

    }
}