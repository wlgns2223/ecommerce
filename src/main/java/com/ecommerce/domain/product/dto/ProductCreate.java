package com.ecommerce.domain.product.dto;

import com.ecommerce.domain.product.vo.ModelNumber;
import com.ecommerce.domain.product.vo.OrderQuantityPolicy;
import com.ecommerce.domain.product.vo.Pricing;
import com.ecommerce.domain.product.vo.SortOrder;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductCreate(
        String name,
        Pricing pricing,
        String description,
        ModelNumber modelNumber,
        Long sellerId,
        String saleUnit,
        OrderQuantityPolicy orderQuantityPolicy,
        ProductCategoriesCreate productCategoryCreate,
        List<OptionGroupCreate> optionGroupCreate
) {
    public ProductCreate {
        if (productCategoryCreate == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        if (orderQuantityPolicy == null) {
            throw new IllegalArgumentException("주문수량 정보는 필수입니다.");
        }

        if (optionGroupCreate == null) {
            optionGroupCreate = List.of();
        }
    }

    @Builder
    public record OptionGroupCreate(
            String name,
            SortOrder sortOrder,
            List<OptionValueCreate> optionValueCreates) {
    }

    @Builder
    public record OptionValueCreate(
            String value,
            SortOrder sortOrder) {
    }

    public record ProductCategoryCreate(
            Long categoryId,
            Boolean isPrimary,
            SortOrder sortOrder) {
    }

    @Builder
    public record ProductCategoriesCreate(
            Long primaryCategoryId,
            List<Long> secondaryCategoryIds
    ) {

        public ProductCategoriesCreate {
            if (primaryCategoryId == null) {
                throw new IllegalArgumentException("대표 카테고리는 필수입니다.");
            }

            List<Long> secondaries = secondaryCategoryIds == null ? List.of() : List.copyOf(secondaryCategoryIds);
            if (secondaries.contains(primaryCategoryId)) {
                throw new IllegalArgumentException("대표 카테고리를 보조 카테고리로 지정할 수 없습니다.");
            }

            if (secondaries.size() != secondaries.stream().distinct().count()) {
                throw new IllegalArgumentException("보조 카테고리에 중복이 있습니다.");
            }

            secondaryCategoryIds = secondaries;
        }
    }


}
