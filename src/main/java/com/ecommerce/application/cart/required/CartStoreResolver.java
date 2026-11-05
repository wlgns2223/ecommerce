package com.ecommerce.application.cart.required;

import com.ecommerce.domain.cart.enums.CartOwnerType;

public interface CartStoreResolver {
    CartStore resolve(CartOwnerType type);
}
