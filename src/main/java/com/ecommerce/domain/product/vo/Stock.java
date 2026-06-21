package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Stock(@Column(name = "stock", nullable = false) int value) {

    public Stock {
        if (value < 0) {
            throw new IllegalArgumentException("재고는 0보다 작을 수 없습니다.");
        }
    }
}
