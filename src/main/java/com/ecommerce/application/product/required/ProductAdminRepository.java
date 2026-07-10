package com.ecommerce.application.product.required;

import com.ecommerce.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAdminRepository extends JpaRepository<Product, Long> {
}
