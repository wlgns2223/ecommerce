package com.ecommerce.application.user.provided;

import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;

/**
 * 회원 등록과 관련된 기능을 제공한다.
 */
public interface UserRegister {
    User register(UserRegisterRequest registerRequest);
}
