package com.ecommerce.application.auth.required;

import com.ecommerce.domain.auth.required.IssuedToken;
import com.ecommerce.domain.user.enums.Role;
import io.jsonwebtoken.Claims;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface TokenProvider {
    IssuedToken createAccessToken(String email, List<Role> roles,String deviceId ,Instant now);

    IssuedToken createAccessToken(String email, String deviceId, Instant now);

    IssuedToken createRefreshToken(String email, List<Role> roles,String deviceId ,Instant now);

    IssuedToken createRefreshToken(String email,String deviceId, Instant now);

    IssuedToken parseToken(String token);

    boolean validate(String token);

    boolean isAccessToken(String token);

}
