package com.ecommerce.adapter.persistence.product;

import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.application.product.required.CategoryChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryCheckerAdapter implements CategoryChecker {

    private final CategoryFinder categoryFinder;

    @Override
    @Transactional(readOnly = true)
    public boolean existsAll(List<Long> ids) {
        return categoryFinder.existsAll(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return categoryFinder.existsById(id);
    }
}
