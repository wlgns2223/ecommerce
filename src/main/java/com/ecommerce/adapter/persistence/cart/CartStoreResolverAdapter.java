package com.ecommerce.adapter.persistence.cart;

import com.ecommerce.adapter.persistence.cart.redis.GuestCartStoreAdapter;
import com.ecommerce.application.cart.required.CartStore;
import com.ecommerce.application.cart.required.CartStoreResolver;
import com.ecommerce.domain.cart.enums.CartOwnerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartStoreResolverAdapter implements CartStoreResolver {

    private final MemberCartStoreAdapter memberCartStoreAdapter;
    private final GuestCartStoreAdapter guestCartStoreAdapter;

    @Override
    public CartStore resolve(CartOwnerType type) {
        return switch (type) {
            case GUEST -> guestCartStoreAdapter;
            case MEMBER -> memberCartStoreAdapter;
        };
    }
}
