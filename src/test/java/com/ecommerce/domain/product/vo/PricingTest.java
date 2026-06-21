package com.ecommerce.domain.product.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PricingTest {

    @Test
    @DisplayName("판매가는 정가보다 클 수 없다.")
    void salePrice() {
        assertThatThrownBy(() ->
                new Pricing(
                        new Money(BigDecimal.valueOf(100L)),
                        new Money(BigDecimal.valueOf(10L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("할인율을 계산한다.")
    void discountRate() {
        // given
        Money salePrice = new Money(BigDecimal.valueOf(7000));
        Money retailPrice = new Money(BigDecimal.valueOf(10_000));
        Pricing pricing = new Pricing(salePrice, retailPrice);

        // when
        BigDecimal discountRate = pricing.discountRate();

        // then
        assertThat(discountRate).isEqualByComparingTo("30.00");

    }

}