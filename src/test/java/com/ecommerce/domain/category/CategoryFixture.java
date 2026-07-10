package com.ecommerce.domain.category;

import com.ecommerce.adapter.webApi.category.dto.request.CategoryCreateRequest;

public class CategoryFixture {
    public static CategoryCreateRequest createCategoryCreateRequest(String slug) {
        return new CategoryCreateRequest("test-name", slug, 1);
    }

    public static CategoryCreateRequest createCategoryCreateRequest(String name, String slug) {
        return new CategoryCreateRequest(name, slug, 1);
    }

    public static CategoryCreateRequest createCategoryCreateRequest() {
        return new CategoryCreateRequest("test-name", "test-slug", 1);
    }

    public static CategoryCreateRequest createCategoryCreateRequest(String slug, Long categoryId) {
        return new CategoryCreateRequest("test-name", slug, 1, categoryId);
    }
}
