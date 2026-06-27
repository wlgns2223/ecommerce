package com.ecommerce.application.auth.required;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.vo.Email;

import java.time.Instant;
import java.util.List;

public interface TokenProvider {
    IssuedToken createAccessToken(Email email, List<Role> roles, String deviceId, Instant now);

    IssuedToken createAccessToken(Email email, String deviceId, Instant now);

    IssuedToken createRefreshToken(Email email, List<Role> roles, String deviceId, Instant now);

    IssuedToken createRefreshToken(Email email, String deviceId, Instant now);

    IssuedToken parseToken(String token);

}
