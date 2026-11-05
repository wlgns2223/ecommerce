package com.ecommerce.application.product.provided;

import com.ecommerce.application.product.provided.dto.request.ProductSkuCreateRequest;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.ProductSku;

public interface ProductModify {

    Product create(ProductCreate productCreate);

}
