package com.ecommerce.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRegisterRequest(
        @NotBlank String name,
        @NotBlank String slug,
        @NotNull Long parentId
) {
}
