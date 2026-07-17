package com.ecommerce.application.auth.required;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.user.enums.Role;

import java.time.Instant;
import java.util.List;

public interface TokenProvider {
    IssuedToken createAccessToken(Long id, List<Role> roles, String deviceId, Instant now);

    IssuedToken createAccessToken(Long id, String deviceId, Instant now);

    IssuedToken createRefreshToken(Long id, List<Role> roles, String deviceId, Instant now);

    IssuedToken createRefreshToken(Long id, String deviceId, Instant now);

    IssuedToken parseToken(String token);

}
