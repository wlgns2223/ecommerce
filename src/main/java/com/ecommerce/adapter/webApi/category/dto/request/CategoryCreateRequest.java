package com.ecommerce.adapter.webApi.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CategoryCreateRequest(
        @NotNull String name,
        @NotBlank String slug,
        @NotNull @Positive Integer level,
        Long parentId
) {

    public CategoryCreateRequest(@NotNull String name, @NotBlank String slug, @NotNull @Positive Integer level, Long parentId) {
        this.name = name;
        this.slug = slug;
        this.level = level;
        this.parentId = parentId;
    }

    public CategoryCreateRequest(String name, String slug, Integer level) {
        this(name, slug, level, null);
    }
}
