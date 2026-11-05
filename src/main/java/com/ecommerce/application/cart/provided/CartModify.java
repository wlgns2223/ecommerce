package com.ecommerce.application.cart.provided;

import com.ecommerce.application.cart.provided.dto.CartItemAddRequest;
import com.ecommerce.domain.cart.entity.Cart;
import com.ecommerce.domain.cart.vo.CartOwner;

public interface CartModify {

    Cart addItem(CartItemAddRequest cartItemAddRequest);

    Cart create(CartOwner cartOwner);
}
