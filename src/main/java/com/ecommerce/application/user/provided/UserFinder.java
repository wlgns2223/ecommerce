package com.ecommerce.application.user.provided;

public interface UserFinder {
    boolean existsById(Long id);
}
