package com.ecommerce.application.category.provided;

import com.ecommerce.domain.category.dto.request.CategoryCreateRequest;
import com.ecommerce.domain.category.entity.Category;

public interface CategoryManagement {
    Category create(CategoryCreateRequest request);

}
