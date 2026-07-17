package com.ecommerce.domain.product.entity;

import com.ecommerce.adapter.webApi.product.dto.request.ProductCreateRequest;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.vo.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductFixture {

    public static ProductCreateRequest createProductCreateRequestWithoutOptions(Long primaryCategoryId) {

        return ProductCreateRequest.builder()
                .name("test-product")
                .salePrice(BigDecimal.valueOf(8_000))
                .retailPrice(BigDecimal.valueOf(10_000))
                .description("description...")
                .modelNumber("PRODUCT-1")
                .saleUnit("1개")
                .orderQuantityPolicyRequest(ProductCreateRequest.OptionQuantityPolicyRequest.builder()
                        .min(1)
                        .max(9999)
                        .orderUnit(1)
                        .build())
                .categoriesRequest(ProductCreateRequest.CategoriesRequest.builder()
                        .primaryCategoryId(primaryCategoryId)
                        .secondaryCategoryIds(List.of())
                        .build())
                .optionGroupCreateRequests(List.of(
                        ProductCreateRequest.OptionGroupCreateRequest.builder()
                                .name("test-option-group")
                                .sortOrder(0)
                                .optionValueCreateRequests(List.of(
                                        ProductCreateRequest.OptionValueCreateRequest.builder()
                                                .value("test-option-value")
                                                .sortOrder(0)
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }

    public static ProductCreate createProductWithoutOption(Long sellerId, Long categoryId) {
        return ProductCreate.builder()
                .name("test-product")
                .pricing(Pricing.builder()
                        .salePrice(new Money(BigDecimal.valueOf(8_000)))
                        .retailPrice(new Money(BigDecimal.valueOf(10_000)))
                        .build())
                .description("description...")
                .modelNumber(new ModelNumber("PRODUCT-1"))
                .sellerId(sellerId)
                .saleUnit("1개")
                .orderQuantityPolicy(OrderQuantityPolicy.builder()
                        .min(1)
                        .max(999)
                        .orderUnit(1)
                        .build())
                .productCategoryCreate(ProductCreate.ProductCategoriesCreate.builder()
                        .primaryCategoryId(categoryId)
                        .build())
                .build();
    }

    public static ProductCreate createProductWithOptions(Long sellerId, Long categoryId) {
        return ProductCreate.builder()
                .name("test-product")
                .pricing(Pricing.builder()
                        .salePrice(new Money(BigDecimal.valueOf(8_000)))
                        .retailPrice(new Money(BigDecimal.valueOf(10_000)))
                        .build())
                .description("description...")
                .modelNumber(new ModelNumber("PRODUCT-1"))
                .sellerId(sellerId)
                .saleUnit("1개")
                .orderQuantityPolicy(OrderQuantityPolicy.builder()
                        .min(1)
                        .max(999)
                        .orderUnit(1)
                        .build())
                .productCategoryCreate(ProductCreate.ProductCategoriesCreate.builder()
                        .primaryCategoryId(categoryId)
                        .build())
                .optionGroupCreate(List.of(
                        ProductCreate.OptionGroupCreate.builder()
                                .name("사이즈")
                                .sortOrder(new SortOrder(0))
                                .optionValueCreates(
                                        List.of(ProductCreate.OptionValueCreate.
                                                        builder()
                                                        .value("L")
                                                        .sortOrder(new SortOrder(0))
                                                        .build(),
                                                ProductCreate.OptionValueCreate.
                                                        builder()
                                                        .value("M")
                                                        .sortOrder(new SortOrder(0))
                                                        .build()
                                                ,
                                                ProductCreate.OptionValueCreate.
                                                        builder()
                                                        .value("S")
                                                        .sortOrder(new SortOrder(0))
                                                        .build()
                                        )
                                )
                                .build()
                ))
                .build();
    }

}
