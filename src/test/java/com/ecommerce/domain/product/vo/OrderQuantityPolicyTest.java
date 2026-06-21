package com.ecommerce.domain.product.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderQuantityPolicyTest {

    @Test
    @DisplayName("최소 주문 수량은 구매단위의 배수이다.")
    void orderUnit() {

        assertThatThrownBy(() -> new OrderQuantityPolicy(1, 10, 2))
                .isInstanceOf(IllegalArgumentException.class);

    }

}