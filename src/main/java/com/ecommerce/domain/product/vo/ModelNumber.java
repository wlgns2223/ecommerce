package com.ecommerce.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ModelNumber(@Column(nullable = false, name = "model_number") String value) {
    

}
