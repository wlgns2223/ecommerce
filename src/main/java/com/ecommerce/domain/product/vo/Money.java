package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Money(@Column(precision = 10, scale = 2, nullable = false)
                    BigDecimal amount) {

    public Money {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("가격은 0보다 낮을 수 없습니다. " +
                    " amount: " + amount
            );
        }
    }
}
