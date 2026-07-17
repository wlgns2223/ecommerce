package com.ecommerce.domain.auth.dto;

import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.user.enums.Role;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record IssuedToken(String token,
                          Long id,
                          List<Role> roles,
                          String deviceId,
                          Instant expiresAt,
                          TokenType tokenType) {

}
