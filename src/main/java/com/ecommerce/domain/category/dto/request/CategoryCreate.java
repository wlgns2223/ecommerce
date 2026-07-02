package com.ecommerce.domain.category.dto.request;

import com.ecommerce.domain.category.entity.Category;
import lombok.Builder;

@Builder
public record CategoryCreate(
        String name,
        String slug,
        Category parent
) {

    public CategoryCreate {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name이 비어있습니다.");
        }

        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("slug가 비어있습니다.");
        }

    }
}
