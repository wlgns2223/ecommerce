package com.ecommerce.application.auth.provided;

import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.user.dto.UserLoginRequest;

public interface Authenticator {

    TokenResult login(UserLoginRequest loginRequest);

    TokenResult renew(String refreshToken);
}
