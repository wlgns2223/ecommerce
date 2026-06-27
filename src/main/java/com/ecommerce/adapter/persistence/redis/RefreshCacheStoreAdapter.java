package com.ecommerce.adapter.persistence.redis;

import com.ecommerce.application.auth.required.CacheStore;
import com.ecommerce.application.auth.required.RefreshCacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshCacheStoreAdapter implements RefreshCacheStore {

    private final CacheStore cacheStore;

    private static final String PREFIX = "refresh:";

    @Override
    public void save(String hashedToken, Long userId, Duration ttl) {
        cacheStore.save(
                PREFIX + hashedToken,
                String.valueOf(userId),
                ttl
        );

    }

    @Override
    public Optional<Long> find(String key) {
        return cacheStore.find(PREFIX + key).map(Long::valueOf);
    }

    @Override
    public void delete(String key) {
        cacheStore.delete(PREFIX + key);
    }
}
