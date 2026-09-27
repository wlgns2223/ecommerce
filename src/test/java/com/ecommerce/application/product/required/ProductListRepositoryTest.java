package com.ecommerce.application.product.required;

import com.ecommerce.adapter.persistence.product.CategoryHierarchyReaderAdapter;
import com.ecommerce.adapter.persistence.product.ProductListRepositoryAdapter;
import com.ecommerce.adapter.persistence.querydsl.config.QueryDslConfig;
import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.application.category.CategoryQueryService;
import com.ecommerce.application.product.provided.dto.request.ProductSearchCondition;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.category.CategoryFixture;
import com.ecommerce.domain.category.dto.request.CategoryCreate;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.ProductFixture;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.required.PasswordEncoder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        TestContainerConfiguration.class,
        QueryDslConfig.class,
        ProductListRepositoryAdapter.class,
        CategoryHierarchyReaderAdapter.class,
        CategoryQueryService.class
})
class ProductListRepositoryTest {

    @Autowired
    ProductListRepository productListRepository;


    @Autowired
    EntityManager entityManager;

    Category category;

    User user;

    Product product;

    @BeforeEach
    void setUp() {
        CategoryCreate categoryCreate = CategoryFixture.createCategoryCreate("상의");
        category = Category.create(categoryCreate);
        entityManager.persist(category);

        UserRegister userRegister = UserFixture.createUserRegister("test@gmail.com");
        PasswordEncoder passwordEncoder = UserFixture.createPasswordEncoder();
        user = User.create(userRegister, passwordEncoder);
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
    void searchProducts() {
        // given

        // when
        Page<ProductSummary> productSummaries = productListRepository.searchProducts(ProductSearchCondition.builder()
                .categoryId(category.getId())
                .build(), PageRequest.of(0, 10));

        // then
        assertThat(productSummaries).isNotEmpty();
        assertThat(productSummaries.getContent())
                .extracting(ProductSummary::name)
                .containsExactly(product.getName());

    }

}