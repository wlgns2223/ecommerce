package com.ecommerce.application.category;

import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.application.category.required.CategoryRepository;
import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryQueryService implements CategoryFinder {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Category find(Long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다. id " + categoryId));
    }
}
