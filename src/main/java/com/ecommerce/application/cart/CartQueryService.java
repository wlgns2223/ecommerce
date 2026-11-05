package com.ecommerce.application.cart;

import com.ecommerce.application.cart.provided.CartFinder;
import com.ecommerce.application.cart.required.CartStoreResolver;
import com.ecommerce.domain.cart.entity.Cart;
import com.ecommerce.domain.cart.vo.CartOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartQueryService implements CartFinder {

    private final CartStoreResolver cartStoreResolver;

    @Override
    public Optional<Cart> findByOwner(CartOwner owner) {
        return cartStoreResolver.resolve(owner.type()).findByOwner(owner.id());
    }
}
