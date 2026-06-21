package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SortOrder(@Column(nullable = false, name = "sort_order") int value) {

    public SortOrder {
        if (value < 0) {
            throw new IllegalArgumentException("순서는 음수가 될 수 없습니다.");
        }
    }
}
