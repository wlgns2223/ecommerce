package com.ecommerce.application.auth.required;

import java.time.Duration;
import java.util.Optional;

public interface CacheStore {
    void save(String key, String value, Duration ttl);

    Optional<String> find(String key);

    void delete(String key);
}
