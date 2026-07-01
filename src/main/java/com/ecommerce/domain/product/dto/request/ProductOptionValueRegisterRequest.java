package com.ecommerce.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProductOptionValueRegisterRequest(@NotNull String value) {
}
