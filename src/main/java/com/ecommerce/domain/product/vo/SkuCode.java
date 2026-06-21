package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record SkuCode(@Column(nullable = false, name = "sku_code") String value) {
    public static SkuCode create() {
        return new SkuCode(UUID.randomUUID().toString());
    }
}
