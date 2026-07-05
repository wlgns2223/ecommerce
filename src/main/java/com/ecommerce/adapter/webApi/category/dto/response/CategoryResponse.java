package com.ecommerce.adapter.webApi.category.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String name,
        Long parentId,
        String slug,
        boolean isActive,
        int level
) {
}
