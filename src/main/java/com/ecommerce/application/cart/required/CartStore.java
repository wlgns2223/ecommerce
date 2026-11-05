package com.ecommerce.application.cart.required;

import com.ecommerce.domain.cart.entity.Cart;

import java.util.Optional;

public interface CartStore {
    Optional<Cart> findByOwner(String id);

    Cart createIfAbsent(String ownerId);
}
