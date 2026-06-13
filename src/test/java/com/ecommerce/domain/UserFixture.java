package com.ecommerce.domain;

import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.UserDetailFixture;
import com.ecommerce.domain.user.required.PasswordEncoder;

public class UserFixture {

    public static UserRegisterRequest createUserRegisterRequest(String email) {
        return new UserRegisterRequest(email,
                "password",
                "foo",
                "01012341234",
                UserDetailFixture.createUserDetailRequest()
        );
    }

    public static UserRegisterRequest createUserRegisterRequest(String email, String password) {
        return new UserRegisterRequest(email,
                password,
                "foo",
                "01012341234",
                UserDetailFixture.createUserDetailRequest()
        );
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString().toUpperCase();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }
}
