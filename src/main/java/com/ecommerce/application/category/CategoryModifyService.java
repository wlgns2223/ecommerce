package com.ecommerce.application.category;

import com.ecommerce.application.category.provided.CategoryModify;
import com.ecommerce.application.category.required.CategoryRepository;
import com.ecommerce.domain.category.dto.request.CategoryCreate;
import com.ecommerce.domain.category.dto.request.CategoryCreateRequest;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.category.exception.DuplicationSlugException;
import com.ecommerce.domain.category.vo.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryModifyService implements CategoryModify {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(CategoryCreateRequest request) {

        if (categoryRepository.existsBySlug(new Slug(request.slug()))) {
            throw new DuplicationSlugException("중복된 slug입니다. slug: " + request.slug());
        }

        Category parent = Optional.ofNullable(request.parentId())
                .flatMap(categoryRepository::findById)
                .orElse(null);

        return categoryRepository.save(Category.create(CategoryCreate
                .builder()
                .name(request.name())
                .parent(parent)
                .slug(request.slug())
                .build()
        ));
    }
}
