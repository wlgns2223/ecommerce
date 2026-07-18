package com.ecommerce.adapter.webApi.product.dto.response;

import com.ecommerce.domain.product.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(

        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        String name,
        String description,
        String modelNumberValue,
        BigDecimal pricingSalePriceAmount,
        BigDecimal pricingRetailPriceAmount,
        Long sellerId,
        String saleUnit,
        int orderQuantityPolicyMin,
        int orderQuantityPolicyMax,
        int orderQuantityPolicyOrderUnit,
        ProductStatus status,
        List<ProductCategoryDto> productCategories,
        List<ProductOptionGroupDto> productOptionGroups
) {
    /**
     * DTO for {@link com.ecommerce.domain.product.entity.ProductCategory}
     */
    public record ProductCategoryDto(Long id,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt,
                                     LocalDateTime deletedAt,
                                     Long categoryId,
                                     Boolean isPrimary,
                                     int sortOrderValue) {

    }

    /**
     * DTO for {@link com.ecommerce.domain.product.entity.ProductOptionGroup}
     */
    public record ProductOptionGroupDto(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            List<ProductOptionValueDto> productOptionValues,
            String name,
            int sortOrderValue) {
    }

    /**
     * DTO for {@link com.ecommerce.domain.product.entity.ProductOptionValue}
     */
    public record ProductOptionValueDto(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            String value,
            int sortOrderValue) {
    }
}
