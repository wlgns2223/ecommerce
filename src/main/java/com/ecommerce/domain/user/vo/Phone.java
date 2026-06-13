package com.ecommerce.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Phone(@Column(nullable = false, length = 20, name = "phone") String value) {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d+");

    public Phone {
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("잘못된 전화번호입니다.  " + value);
        }
    }
}
