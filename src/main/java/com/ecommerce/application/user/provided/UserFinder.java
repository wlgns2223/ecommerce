package com.ecommerce.application.user.provided;

import com.ecommerce.domain.user.entity.User;

import java.util.Optional;

public interface UserFinder {
    boolean existsById(Long id);

    Optional<User> findById(Long userId);
}
