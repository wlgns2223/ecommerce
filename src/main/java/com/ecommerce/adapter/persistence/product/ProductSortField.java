package com.ecommerce.adapter.persistence.product;

import com.ecommerce.adapter.persistence.querydsl.support.SortField;
import com.ecommerce.domain.product.entity.QProduct;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public enum ProductSortField implements SortField {

    CREATED_AT("createdAt", QProduct.product.createdAt),
    NAME("name", QProduct.product.name),
    SALE_PRICE("salePrice", QProduct.product.pricing.salePrice.amount);

    private final String key;

    private final ComparableExpressionBase<?> expression;

    ProductSortField(String key, ComparableExpressionBase<?> expressionBase) {

        this.key = key;
        this.expression = expressionBase;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public ComparableExpressionBase<?> expression() {
        return expression;
    }
}
