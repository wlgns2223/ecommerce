package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ModelNumber(@Column(nullable = false, name = "model_number") String value) {

    public ModelNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Model Number는 null이거나 빈 값일 수 없습니다.");
        }
    }
}
