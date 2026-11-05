package com.ecommerce.adapter.persistence.cart.redis;

import com.ecommerce.application.cart.required.CartStore;
import com.ecommerce.domain.cart.entity.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuestCartStoreAdapter implements CartStore {

    private static final String GUEST_CART_PREFIX = "cart:guest:";
    private static final Duration GUEST_CART_TTL = Duration.ofDays(30);
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static String genKey(String ownerId) {
        return GUEST_CART_PREFIX + ownerId;
    }

    @Override
    public Optional<Cart> findByOwner(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("비회원 장바구니 아이디가 필요합니다.");
        }

        String cartInJson = redisTemplate.opsForValue().get(GUEST_CART_PREFIX + id);
        if (cartInJson == null) {
            return Optional.empty();
        }

        try {
            Cart cart = objectMapper.readValue(cartInJson, Cart.class);
            if (cart == null) {
                throw new IllegalStateException("장바구니 데이터가 null입니다.");
            }

            return Optional.of(cart);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cart createIfAbsent(String ownerId) {
        if (ownerId == null || ownerId.isBlank()) {
            throw new IllegalArgumentException("비회원 장바구니 아이디가 필요합니다.");
        }

        try {
            Cart cart = Cart.create(null);
            String cartString = objectMapper.writeValueAsString(cart);

            // 조회 후 저장하면 동시 요청이 서로 덮어쓸 수 있으므로 SET NX로 원자적으로 생성한다.
            Boolean created = redisTemplate.opsForValue()
                    .setIfAbsent(genKey(ownerId), cartString, GUEST_CART_TTL);
            if (Boolean.TRUE.equals(created)) {
                return cart;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return findByOwner(ownerId)
                .orElseThrow(() -> new IllegalStateException("비회원 장바구니를 찾을 수 없습니다."));
    }
}
