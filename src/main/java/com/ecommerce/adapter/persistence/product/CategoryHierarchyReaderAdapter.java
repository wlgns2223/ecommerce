package com.ecommerce.adapter.persistence.product;

import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.application.product.required.CategoryHierarchyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryHierarchyReaderAdapter implements CategoryHierarchyReader {

    private final CategoryFinder categoryFinder;

    @Override
    public List<Long> findSelfAndDescendantIds(Long rootId) {
        return categoryFinder.findSelfAndDescendantIds(rootId);
    }
}
