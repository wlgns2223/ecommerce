package com.ecommerce.application.product.required;

import com.ecommerce.domain.product.entity.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
}
