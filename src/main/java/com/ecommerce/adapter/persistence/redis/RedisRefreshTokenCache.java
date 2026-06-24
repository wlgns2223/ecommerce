package com.ecommerce.adapter.persistence.redis;

import com.ecommerce.application.auth.required.RefreshTokenCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenCache implements RefreshTokenCache {

    private static final String PREFIX = "refresh:";
    private final StringRedisTemplate redisTemplate;

    @NonNull
    private static String getKey(String email) {
        return PREFIX + email;
    }

    @Override
    public void save(String email, String refreshToken, Duration ttl) {
        redisTemplate.opsForValue().set(getKey(email), refreshToken, ttl);
    }

    @Override
    public Optional<String> find(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(email)));
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(getKey(email));
    }
}
