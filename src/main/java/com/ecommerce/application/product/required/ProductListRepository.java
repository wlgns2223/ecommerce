package com.ecommerce.application.product.required;

import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.application.product.provided.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductListRepository {

    Page<ProductSummary> searchProducts(ProductSearchCondition searchCondition, Pageable pageable);
}
