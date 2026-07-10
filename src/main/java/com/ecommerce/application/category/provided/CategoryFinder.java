package com.ecommerce.application.category.provided;

import com.ecommerce.domain.category.entity.Category;

import java.util.List;

public interface CategoryFinder {

    Category find(Long categoryId);

    boolean existsAll(List<Long> ids);
}
