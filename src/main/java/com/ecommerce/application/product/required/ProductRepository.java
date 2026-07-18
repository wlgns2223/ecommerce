package com.ecommerce.application.product.required;

import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndStatus(Long id, ProductStatus status);
}
