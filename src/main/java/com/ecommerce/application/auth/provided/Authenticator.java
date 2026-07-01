package com.ecommerce.application.auth.provided;

import com.ecommerce.domain.auth.dto.request.UserDeleteRequestDto;
import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.vo.Email;

public interface Authenticator {

    TokenResult login(UserLoginRequest loginRequest);

    TokenResult renew(String refreshToken);

    void logout(String refreshToken);

    void delete(Email email, UserDeleteRequestDto deleteRequestDto, String refreshToken);
}
