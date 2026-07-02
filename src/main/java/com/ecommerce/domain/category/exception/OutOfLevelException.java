package com.ecommerce.domain.category.exception;

public class OutOfLevelException extends RuntimeException {
    public OutOfLevelException(String message) {
        super(message);
    }
}
