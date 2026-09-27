package com.ecommerce.adapter.webApi.product;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.adapter.webApi.shared.dto.response.PageResponse;
import com.ecommerce.application.product.provided.dto.request.ProductSearchCondition;
import com.ecommerce.config.TestConfig;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.category.CategoryFixture;
import com.ecommerce.domain.category.dto.request.CategoryCreate;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.ProductFixture;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.required.PasswordEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@Import({TestContainerConfiguration.class, TestConfig.class})
@AutoConfigureMockMvc
@Transactional
class ProductApiTest {

    private static final String EMAIL = "test@gmail.com";

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    Category category;

    Product product;

    @BeforeEach
    void setUp() {
        CategoryCreate categoryCreate = CategoryFixture.createCategoryCreate("상의");
        category = Category.create(categoryCreate);
        entityManager.persist(category);

        UserRegister userRegister = UserFixture.createUserRegister(EMAIL);
        User user = User.create(userRegister, passwordEncoder);
        entityManager.persist(user);
        entityManager.flush();

        ProductCreate productCreate = ProductFixture.createProductWithOptions(user.getId(), category.getId());
        product = Product.create(productCreate);
        product.markOnSale();
        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("목록조회를 할 수 있다.")
    void searchProducts() throws JsonProcessingException, UnsupportedEncodingException {
        // given
        MvcTestResult loginResult = loginAndGetResult();

        ProductSearchCondition conditions = ProductFixture.createProductSearchConditions();
        String conditionsJson = objectMapper.writeValueAsString(conditions);

        // when
        MvcTestResult searchResult = mockMvcTester.get().uri("/api/product")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(loginResult.getResponse().getCookies())
                .content(conditionsJson)
                .exchange();

        ApiResponse<PageResponse<ProductSummary>> resultPage = objectMapper.readValue(searchResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<PageResponse<ProductSummary>>>() {
                });
        // then
        assertThat(searchResult).hasStatusOk();
        assertThat(resultPage.getData().content()).extracting(ProductSummary::name)
                .containsExactly(product.getName());

    }

    private @NotNull MvcTestResult loginAndGetResult() throws JsonProcessingException {
        UserLoginRequest loginRequest = UserFixture.createLoginRequest(EMAIL);
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
        return mockMvcTester.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson)
                .exchange();
    }

}