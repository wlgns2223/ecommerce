package com.ecommerce.domain.auth.required;

public interface TokenHasher {
    String hash(String raw);

    boolean matches(String rawToken, String hashedToken);
}
