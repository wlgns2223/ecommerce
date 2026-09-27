package com.ecommerce.adapter.webApi.product;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.product.dto.response.ProductResponse;
import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.adapter.webApi.product.mapper.ProductMapper;
import com.ecommerce.adapter.webApi.shared.dto.response.PageResponse;
import com.ecommerce.application.product.provided.ProductFinder;
import com.ecommerce.application.product.provided.dto.request.ProductSearchCondition;
import com.ecommerce.domain.product.entity.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductFinder productFinder;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> findById(@PathVariable Long id) {
        Product product = productFinder.findById(id);
        return ApiResponse.ok(productMapper.toResponse(product));
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductSummary>> searchProducts(@Valid @ModelAttribute ProductSearchCondition searchCondition,
                                                                    @PageableDefault Pageable pageable) {
        Page<ProductSummary> productSummaryPage = productFinder.searchProducts(searchCondition, pageable);
        return ApiResponse.ok(PageResponse.from(productSummaryPage));
    }
}
