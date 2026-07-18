package com.ecommerce.adapter.webApi.product;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.product.dto.response.ProductResponse;
import com.ecommerce.adapter.webApi.product.mapper.ProductMapper;
import com.ecommerce.application.product.provided.ProductFinder;
import com.ecommerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductFinder productFinder;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    ApiResponse<ProductResponse> findById(@PathVariable Long id) {
        Product product = productFinder.findById(id);
        return ApiResponse.ok(productMapper.toResponse(product));
    }
}
