package com.ecommerce.application.product.provided;

import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;

public interface ProductModify {

    Product create(ProductCreate productCreate);

}
