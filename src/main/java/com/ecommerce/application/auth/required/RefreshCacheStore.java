package com.ecommerce.application.auth.required;

import java.time.Duration;
import java.util.Optional;

public interface RefreshCacheStore {
    void save(String hashedToken, Long userId, Duration ttl);

    Optional<Long> find(String key);

    void delete(String key);
}
