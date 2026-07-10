package com.ecommerce.application.category.provided;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;
import com.ecommerce.domain.category.entity.Category;

public interface CategoryModify {
    Category create(CategoryCreateRequest request);
}
