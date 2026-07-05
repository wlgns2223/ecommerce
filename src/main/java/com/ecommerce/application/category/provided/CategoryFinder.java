package com.ecommerce.application.category.provided;

import com.ecommerce.domain.category.entity.Category;

public interface CategoryFinder {

    Category find(Long categoryId);
}
