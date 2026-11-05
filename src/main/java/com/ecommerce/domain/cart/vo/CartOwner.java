package com.ecommerce.domain.cart.vo;

import com.ecommerce.domain.cart.enums.CartOwnerType;

public record CartOwner(CartOwnerType type, String id) {
}
