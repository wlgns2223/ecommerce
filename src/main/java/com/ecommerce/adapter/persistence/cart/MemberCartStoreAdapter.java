package com.ecommerce.adapter.persistence.cart;

import com.ecommerce.application.cart.required.CartRepository;
import com.ecommerce.application.cart.required.CartStore;
import com.ecommerce.application.user.provided.UserFinder;
import com.ecommerce.domain.cart.entity.Cart;
import com.ecommerce.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberCartStoreAdapter implements CartStore {

    private final CartRepository cartRepository;
    private final UserFinder userFinder;

    @Override
    public Optional<Cart> findByOwner(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("회원 장바구니 아이디가 필요합니다.");
        }

        return cartRepository.findByUserId(Long.valueOf(id));
    }

    @Override
    public Cart createIfAbsent(String ownerId) {
        return cartRepository.findByUserId(Long.valueOf(ownerId)).orElseGet(() -> {
            User user = userFinder.findById(Long.valueOf(ownerId))
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
            return cartRepository.save(Cart.create(user));
        });
    }
}
