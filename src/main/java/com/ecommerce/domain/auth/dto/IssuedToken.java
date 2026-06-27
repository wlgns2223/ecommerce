package com.ecommerce.domain.auth.dto;

import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.vo.Email;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record IssuedToken(String token,
                          Email email,
                          List<Role> roles,
                          String deviceId,
                          Instant expiresAt,
                          TokenType tokenType) {

}
