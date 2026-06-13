package com.ecommerce.adapter.webApi.user;

import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthApiTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("회원가입을 한다.")
    void register() throws JsonProcessingException {
        // given
        UserRegisterRequest request = UserFixture.createUserRegisterRequest("foo@gmail.com");

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        // then
        assertThat(result).hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.data.id", (value) -> assertThat(value).isNotNull());

    }
}