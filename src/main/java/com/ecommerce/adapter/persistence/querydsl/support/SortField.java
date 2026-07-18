package com.ecommerce.adapter.persistence.querydsl.support;

import com.querydsl.core.types.dsl.ComparableExpressionBase;

public interface SortField {
    String key();

    ComparableExpressionBase<?> expression();
}
