package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @DisplayName("로그인")
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
}