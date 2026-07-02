package com.ecommerce.domain.category.exception;

public class DuplicationSlugException extends RuntimeException {
    public DuplicationSlugException(String message) {
        super(message);
    }
}
