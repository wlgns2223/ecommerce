package com.ecommerce.adapter.webApi.product;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;
import com.ecommerce.adapter.webApi.category.dto.response.CategoryResponse;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.product.dto.request.ProductCreateRequest;
import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.category.CategoryFixture;
import com.ecommerce.domain.product.entity.ProductFixture;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegister;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
class ProductAdminApiTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("product create api")
    void create() throws JsonProcessingException, UnsupportedEncodingException {
        // given
        UserRegister registerRequest = UserFixture.createUserRegister("admin@gmail.com");
        ApiResponse<UserResponse> userResponseApiResponse = registerUser(registerRequest);
        MvcTestResult loginResult = doLogin(registerRequest);
        ApiResponse<CategoryResponse> categoryResponse = createTestCategory(loginResult);


        ProductCreateRequest productCreateRequest = ProductFixture.createProductCreateRequestWithoutOptions(
                categoryResponse.getData().id()
        );
        String productCreateRequestJson = objectMapper.writeValueAsString(productCreateRequest);

        // when
        MvcTestResult result = mockMvcTester.post().uri("/api/admin/product")
                .with(csrf())
                .cookie(loginResult.getResponse().getCookies())
                .contentType(MediaType.APPLICATION_JSON)
                .content(productCreateRequestJson)
                .exchange();

        // then
        assertThat(result).hasStatus(HttpStatus.CREATED);

    }

    private @NotNull ApiResponse<UserResponse> registerUser(UserRegister registerRequest) throws JsonProcessingException, UnsupportedEncodingException {

        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);
        MvcTestResult registrationResponse = mockMvcTester.post().uri("/api/auth/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson)
                .exchange();

        return objectMapper.readValue(registrationResponse.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<UserResponse>>() {
                });
    }

    private @NotNull MvcTestResult doLogin(UserRegister registerRequest) throws JsonProcessingException {
        UserLoginRequest loginRequest = UserFixture.createLoginRequest(registerRequest.email(), registerRequest.password());
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        return mockMvcTester.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson)
                .exchange();
    }

    private ApiResponse<CategoryResponse> createTestCategory(MvcTestResult loginResult) throws JsonProcessingException, UnsupportedEncodingException {
        CategoryCreateRequest categoryCreateRequest = CategoryFixture.createCategoryCreateRequest();
        String categoryRequestJson = objectMapper.writeValueAsString(categoryCreateRequest);
        MvcTestResult categoryCreationResponse = mockMvcTester.post().uri("/api/admin/category")
                .with(csrf())
                .cookie(loginResult.getResponse().getCookies())
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryRequestJson)
                .exchange();

        return objectMapper.readValue(categoryCreationResponse.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<CategoryResponse>>() {
                });
    }

}