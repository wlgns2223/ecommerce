package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public record OrderQuantityPolicy(
        @Column(name = "min_order_qty", nullable = false)
        int min,

        @Column(name = "max_order_qty", nullable = false)
        int max,

        @Column(name = "order_unit", nullable = false)
        int orderUnit
) {
    private static final int MIN_QTY = 0;
    private static final int MAX_QTY = 9999;

    @Builder
    public OrderQuantityPolicy {

        if (min < MIN_QTY) {
            throw new IllegalArgumentException("최소 주문 수량은 1 이상이어야합니다.");
        }

        if (max > MAX_QTY) {
            throw new IllegalArgumentException("최대 주문 수량은 " + MAX_QTY + "입니다.");
        }

        if (max < min) {
            throw new IllegalArgumentException("최대 주문 수량은 최소 주문 수량 이상이어야 합니다.");
        }

        if (orderUnit < 1) {
            throw new IllegalArgumentException("주문 단위는 1이상이어야합니다.");
        }

        if (min % orderUnit != 0) {
            throw new IllegalArgumentException("최소 주문 수량은 주문 단위의 배수여야합니다.");
        }


    }
}
