package com.ecommerce.domain.product.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProductOptionGroupRegisterRequest(
        @NotBlank String name,
        List<ProductOptionValueRegisterRequest> productOptionValueRegisterRequests
) {
}
