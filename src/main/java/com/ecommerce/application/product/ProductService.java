package com.ecommerce.application.product;

import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.application.category.provided.CategoryFinder;
import com.ecommerce.application.product.provided.ProductFinder;
import com.ecommerce.application.product.provided.dto.ProductSearchCondition;
import com.ecommerce.application.product.required.ProductListRepository;
import com.ecommerce.application.product.required.ProductRepository;
import com.ecommerce.application.user.provided.UserFinder;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.enums.ProductStatus;
import com.ecommerce.domain.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductFinder {

    private final ProductRepository productRepository;
    private final ProductListRepository productListRepository;
    private final CategoryFinder categoryFinder;
    private final UserFinder userFinder;

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.ON_SALE)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다. id: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummary> searchProducts(ProductSearchCondition searchCondition, Pageable pageable) {
        if (searchCondition.categoryId() != null && !categoryFinder.existsById(searchCondition.categoryId())) {
            throw new NotFoundException("존재하지 않는 카테고리입니다.");
        }

        if (searchCondition.sellerId() != null && !userFinder.existsById(searchCondition.sellerId())) {
            throw new NotFoundException("존재하지 않는 판매자 입니다.");
        }

        return productListRepository.searchProducts(searchCondition, pageable);
    }
}
