package com.ecommerce.domain.auth.required;

import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.user.enums.Role;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record IssuedToken(String token,
                          String email,
                          List<Role> roles,
                          String deviceId,
                          Instant expiresAt,
                          TokenType tokenType) {
}
