package com.ecommerce.application.cart.provided;

import com.ecommerce.domain.cart.entity.Cart;
import com.ecommerce.domain.cart.vo.CartOwner;

import java.util.Optional;

public interface CartFinder {
    Optional<Cart> findByOwner(CartOwner owner);
}
