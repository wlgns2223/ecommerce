package com.ecommerce.domain.product.dto;

import com.ecommerce.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.*;

import java.util.List;

public record ProductRegisterRequest(
        @NotBlank String name,
        String description,
        @NotNull String saleUnit,
        @Positive @Min(0) int minOrderQty,
        @Positive @Max(9999) int maxOrderQty,
        @Positive int orderUnit,
        @NotEmpty List<ProductOptionValueRegisterRequest> productOptionValueRegisterRequest,
        @NotEmpty List<ProductCategory> categories
) {
}
