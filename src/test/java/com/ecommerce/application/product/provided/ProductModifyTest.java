package com.ecommerce.application.product.provided;

import com.ecommerce.application.category.provided.CategoryModify;
import com.ecommerce.application.user.provided.UserRegister;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.category.CategoryFixture;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.ProductFixture;
import com.ecommerce.domain.product.entity.ProductOptionGroup;
import com.ecommerce.domain.product.entity.ProductOptionValue;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestContainerConfiguration.class)
@Transactional
@Profile("test")
class ProductModifyTest {

    @Autowired
    ProductModify productModify;

    @Autowired
    UserRegister userRegister;

    @Autowired
    CategoryModify categoryModify;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("option없는 product를 생성한다.")
    void createWithoutOptions() {
        // given
        User user = userRegister.register(UserFixture.createUserRegister("test@gmail.com"));
        Category category = categoryModify.create(CategoryFixture.createCategoryCreateRequest());
        ProductCreate productCreate = ProductFixture.createProductWithoutOption(user.getId(), category.getId());

        // when
        Product product = productModify.create(productCreate);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getProductOptionGroups()).isEmpty();
    }

    @Test
    @DisplayName("Option이 잆는 Product를 생성한다.")
    void createWithOptions() {
        // given
        User user = userRegister.register(UserFixture.createUserRegister("test@gmail.com"));
        Category category = categoryModify.create(CategoryFixture.createCategoryCreateRequest());
        ProductCreate productCreate = ProductFixture.createProductWithOptions(user.getId(), category.getId());

        // when
        Product product = productModify.create(productCreate);

        // then
        assertThat(product).isNotNull();
        List<ProductOptionGroup> groups = product.getProductOptionGroups();
        assertThat(groups).hasSize(1);

        ProductOptionGroup group = groups.get(0);
        assertThat(group.getName()).isEqualTo("사이즈");
        assertThat(group.getProductOptionValues())
                .extracting(ProductOptionValue::getValue)
                .containsExactlyInAnyOrder("L", "M", "S");

    }

}