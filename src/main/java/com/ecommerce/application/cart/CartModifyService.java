package com.ecommerce.application.cart;

import com.ecommerce.application.cart.provided.CartModify;
import com.ecommerce.application.cart.provided.dto.CartItemAddRequest;
import com.ecommerce.application.cart.required.CartStoreResolver;
import com.ecommerce.domain.cart.entity.Cart;
import com.ecommerce.domain.cart.vo.CartOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartModifyService implements CartModify {

    private final CartStoreResolver cartStoreResolver;

    @Override

    public Cart addItem(CartItemAddRequest cartItemAddRequest) {
        // cart를 찾는다
        // 없으면 만들고 있으면 카트를 가져온다.
        // cart item entity를 만들어야함
        // skuId로

        return null;
    }

    @Override
    public Cart create(CartOwner cartOwner) {

        return cartStoreResolver.resolve(cartOwner.type())
                .createIfAbsent(cartOwner.id());
    }
}
