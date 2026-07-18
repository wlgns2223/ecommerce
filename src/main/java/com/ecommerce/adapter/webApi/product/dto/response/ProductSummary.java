package com.ecommerce.adapter.webApi.product.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@QueryProjection
public record ProductSummary(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String name,
        String description,
        String modelNumberValue,
        BigDecimal pricingSalePriceAmount,
        BigDecimal pricingRetailPriceAmount
) {
}
