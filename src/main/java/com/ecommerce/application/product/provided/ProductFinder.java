package com.ecommerce.application.product.provided;

import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.application.product.provided.dto.request.ProductSearchCondition;
import com.ecommerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductFinder {
    Product findById(Long productId);

    Page<ProductSummary> searchProducts(ProductSearchCondition searchCondition, Pageable pageable);
}
