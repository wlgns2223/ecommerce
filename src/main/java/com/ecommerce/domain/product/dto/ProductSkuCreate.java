package com.ecommerce.domain.product.dto;

import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.entity.SkuOptionValue;
import com.ecommerce.domain.product.vo.Money;
import com.ecommerce.domain.product.vo.SkuCode;
import com.ecommerce.domain.product.vo.Stock;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductSkuCreate(SkuCode skuCode,
                               Money price,
                               Stock stock,
                               Product product,
                               List<SkuOptionValue> skuOptionValues) {
}
