package com.ecommerce.application.auth.provided;

import com.ecommerce.domain.user.dto.UserLoginRequest;

public interface Authenticator {

    String login(UserLoginRequest loginRequest);
}
