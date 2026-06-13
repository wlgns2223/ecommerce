package com.ecommerce.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Nickname(@Column(nullable = false, length = 100, name = "nickname") String value) {

    public Nickname {
        if (value.isBlank()) {
            throw new IllegalArgumentException("잘못된 닉네임 형식입니다.");
        }
    }
}
