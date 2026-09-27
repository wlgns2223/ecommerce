package com.ecommerce.application.product.provided.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductSearchCondition(
        Long categoryId,
        String keyword,
        @PositiveOrZero BigDecimal minPrice,
        @PositiveOrZero BigDecimal maxPrice,
        Long sellerId
) {
}
