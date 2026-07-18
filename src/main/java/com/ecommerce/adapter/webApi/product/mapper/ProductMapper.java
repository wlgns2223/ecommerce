package com.ecommerce.adapter.webApi.product.mapper;

import com.ecommerce.adapter.webApi.product.dto.response.ProductResponse;
import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.ProductCategory;
import com.ecommerce.domain.product.entity.ProductOptionGroup;
import com.ecommerce.domain.product.entity.ProductOptionValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "pricing.retailPrice.amount", target = "pricingRetailPriceAmount")
    @Mapping(source = "pricing.salePrice.amount", target = "pricingSalePriceAmount")
    @Mapping(source = "modelNumber.value", target = "modelNumberValue")
    @Mapping(source = "orderQuantityPolicy.min", target = "orderQuantityPolicyMin")
    @Mapping(source = "orderQuantityPolicy.max", target = "orderQuantityPolicyMax")
    @Mapping(source = "orderQuantityPolicy.orderUnit", target = "orderQuantityPolicyOrderUnit")
    ProductResponse toResponse(Product product);

    @Mapping(source = "sortOrder.value", target = "sortOrderValue")
    ProductResponse.ProductCategoryDto toCategoryDto(ProductCategory productCategory);

    @Mapping(source = "sortOrder.value", target = "sortOrderValue")
    ProductResponse.ProductOptionGroupDto toProductOptionGroupDto(ProductOptionGroup productOptionGroup);

    @Mapping(source = "sortOrder.value", target = "sortOrderValue")
    ProductResponse.ProductOptionValueDto toProductOptionValueDto(ProductOptionValue productOptionValue);


    @Mapping(source = "modelNumber.value", target = "modelNumberValue")
    @Mapping(source = "pricing.retailPrice.amount", target = "pricingRetailPriceAmount")
    @Mapping(source = "pricing.salePrice.amount", target = "pricingSalePriceAmount")
    ProductSummary toProductSummaryResponse(Product product);
}
