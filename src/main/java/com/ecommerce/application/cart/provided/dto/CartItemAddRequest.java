package com.ecommerce.application.cart.provided.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemAddRequest(
        @NotNull Long skuId,
        @Positive int quantity
) {
}
