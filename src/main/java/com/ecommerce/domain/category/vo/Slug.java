package com.ecommerce.domain.category.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Slug(@Column(name = "slug", nullable = false) String value) {
    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-zA-Z-]+$");

    public Slug {
        if (!SLUG_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("slug는 대소문자와 -만 가능합니다. 입력: " + value);
        }
    }
}
