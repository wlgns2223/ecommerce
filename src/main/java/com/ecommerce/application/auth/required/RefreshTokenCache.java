package com.ecommerce.application.auth.required;

import java.time.Duration;
import java.util.Optional;

public interface RefreshTokenCache {
    void save(String email, String refreshToken, Duration ttl);

    Optional<String> find(java.lang.String email);

    void delete(String email);
}
