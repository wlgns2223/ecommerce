package com.ecommerce.domain.product.vo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public record Pricing(
        @AttributeOverride(name = "amount",
                column = @Column(name = "sale_price", precision = 10, scale = 2, nullable = false)

        )
        // 판매가
        Money salePrice,

        @AttributeOverride(name = "amount",
                column = @Column(name = "retail_price", precision = 10, scale = 2, nullable = false)

        )
        // 원가
        Money retailPrice
) {

    private static final int DIVIDING_SCALE = 10;
    private static final int DECIMAL_SCALE = 2;
    private static final int HUNDRED = 100;

    public Pricing {
        if (salePrice.amount().compareTo(retailPrice.amount()) > 0) {
            throw new IllegalArgumentException("판매가는 정가보다 클 수 없습니다.");
        }
    }

    public BigDecimal discountRate() {
        if (retailPrice.amount().signum() == 0) return BigDecimal.ZERO;
        BigDecimal diff = retailPrice.amount().subtract(salePrice.amount());
        return diff.divide(retailPrice.amount(), DIVIDING_SCALE, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(HUNDRED))
                .setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);

    }
}
