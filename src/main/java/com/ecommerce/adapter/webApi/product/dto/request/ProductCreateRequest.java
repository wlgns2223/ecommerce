package com.ecommerce.adapter.webApi.product.dto.request;

import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.vo.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductCreateRequest(

        @NotBlank String name,
        @NotNull @Positive BigDecimal salePrice,
        @NotNull @Positive BigDecimal retailPrice,
        String description,
        @NotBlank String modelNumber,
        @NotBlank String saleUnit,
        @NotNull @Valid OptionQuantityPolicyRequest orderQuantityPolicyRequest,
        @NotNull @Valid CategoriesRequest categoriesRequest,
        @NotNull List<@Valid OptionGroupCreateRequest> optionGroupCreateRequests

) {

    @Builder
    public record OptionQuantityPolicyRequest(int min, int max, int orderUnit) {
    }

    @Builder
    public record CategoriesRequest(
            @NotNull Long primaryCategoryId, List<Long> secondaryCategoryIds) {
    }

    @Builder
    public record OptionGroupCreateRequest(
            @NotBlank String name,
            int sortOrder,
            List<@Valid OptionValueCreateRequest> optionValueCreateRequests) {
    }

    @Builder
    public record OptionValueCreateRequest(
            @NotBlank String value,
            int sortOrder) {
    }

    public ProductCreate toProductCreate(Long sellerId) {

        return ProductCreate.builder()
                .name(name)
                .pricing(new Pricing(new Money(salePrice), new Money(retailPrice)))
                .description(description)
                .modelNumber(new ModelNumber(modelNumber))
                .sellerId(sellerId)
                .saleUnit(saleUnit)
                .orderQuantityPolicy(OrderQuantityPolicy.builder()
                        .min(orderQuantityPolicyRequest().min())
                        .max(orderQuantityPolicyRequest().max())
                        .orderUnit(orderQuantityPolicyRequest().orderUnit())
                        .build())
                .productCategoryCreate(ProductCreate.ProductCategoriesCreate.builder()
                        .primaryCategoryId(categoriesRequest.primaryCategoryId())
                        .secondaryCategoryIds(categoriesRequest.secondaryCategoryIds())
                        .build())
                .optionGroupCreate(optionGroupCreateRequests
                        .stream()
                        .map((request) -> ProductCreate.OptionGroupCreate
                                .builder()
                                .name(request.name())
                                .sortOrder(new SortOrder(request.sortOrder()))
                                .optionValueCreates(request.optionValueCreateRequests()
                                        .stream()
                                        .map((valueRequest) -> ProductCreate.OptionValueCreate
                                                .builder()
                                                .value(valueRequest.value())
                                                .sortOrder(new SortOrder(valueRequest.sortOrder()))
                                                .build())
                                        .toList()
                                )
                                .build())
                        .toList()
                )
                .build();
    }


}
