package com.ecommerce.application.category.provided;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;
import com.ecommerce.application.category.required.CategoryRepository;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.category.CategoryFixture;
import com.ecommerce.domain.category.Level;
import com.ecommerce.domain.category.dto.request.CategoryCreate;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.category.exception.DuplicationSlugException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestContainerConfiguration.class)
@Transactional
@Profile("test")
class CategoryModifyTest {

    @Autowired
    CategoryModify categoryModify;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("최상단 카테고리 생성. 최상단 카테고리는 부모 카테고리가 null이다. 카테고리를 반환한다. ")
    void create() {
        // given

        CategoryCreateRequest categoryCreateRequest = CategoryFixture.createCategoryCreateRequest();

        // when
        Category category = categoryModify.create(categoryCreateRequest);

        // then
        assertThat(category).isNotNull();
        assertThat(category.getParent()).isNull();
        assertThat(category.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("같은 slug로 생성되면 오류가 난다.")
    void uniqueSlug() {
        // given
        CategoryCreateRequest request = CategoryFixture.createCategoryCreateRequest("unique-slug");
        CategoryCreate categoryCreate = CategoryCreate.builder()
                .name(request.name())
                .slug(request.slug())
                .build();
        categoryRepository.save(Category.create(categoryCreate));

        // then
        assertThatThrownBy(() -> categoryModify.create(request)).isInstanceOf(DuplicationSlugException.class);

    }

    @Test
    @DisplayName("자식 카테고리로 생성 할 수 있다.")
    void parent() {
        // given
        CategoryCreateRequest request = CategoryFixture.createCategoryCreateRequest();
        CategoryCreate categoryCreate = CategoryCreate.builder()
                .name(request.name())
                .slug(request.slug())
                .build();
        Category parent = categoryRepository.save(Category.create(categoryCreate));

        CategoryCreateRequest childrenRequest = CategoryFixture.createCategoryCreateRequest("children", parent.getId());

        //when
        Category category = categoryModify.create(childrenRequest);

        // then
        assertThat(category).isNotNull();
        assertThat(category.getParent().getId()).isEqualTo(parent.getId());
    }

    @Test
    @DisplayName("자신 카테고리는 부모 카테고리 레벨에서 1을 더한다.")
    void addLevelOneFromParent() {
        CategoryCreateRequest request = CategoryFixture.createCategoryCreateRequest();
        CategoryCreate categoryCreate = CategoryCreate.builder()
                .name(request.name())
                .slug(request.slug())
                .build();
        Category parent = categoryRepository.save(Category.create(categoryCreate));

        CategoryCreateRequest childrenRequest = CategoryFixture.createCategoryCreateRequest("children", parent.getId());

        //when
        Category category = categoryModify.create(childrenRequest);

        // then
        assertThat(category).isNotNull();
        assertThat(category.getLevel()).isEqualTo(Level.addLevel(parent.getLevel()));
    }

}