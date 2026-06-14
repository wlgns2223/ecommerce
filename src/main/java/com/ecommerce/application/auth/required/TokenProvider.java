package com.ecommerce.application.auth.required;

import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.List;

public interface TokenProvider {
    String createAccessToken(String email, List<Role> roles, Date now);

    String createRefreshToken(String email, Date now);

    Claims parseClaim(String token);

    boolean validate(String token);

    boolean isAccessToken(String token);
}
