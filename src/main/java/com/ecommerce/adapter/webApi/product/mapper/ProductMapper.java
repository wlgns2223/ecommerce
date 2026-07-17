package com.ecommerce.adapter.webApi.product.mapper;

import com.ecommerce.adapter.webApi.product.dto.response.ProductResponse;
import com.ecommerce.domain.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);
}
