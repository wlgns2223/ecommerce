package com.ecommerce.domain.user;

import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.UserDetailFixture;
import com.ecommerce.domain.user.required.PasswordEncoder;

public class UserFixture {

    private static final String PASSWORD = "password";

    public static UserLoginRequest createLoginRequest(String email) {
        return createLoginRequest(email, PASSWORD);
    }

    public static UserLoginRequest createLoginRequest(String email, String password) {
        return new UserLoginRequest(email, password, "device-1");
    }

    public static UserRegister createUserRegister(String email) {
        return new UserRegister(email,
                PASSWORD,
                "foo",
                "01012341234",
                UserDetailFixture.createUserDetailRequest()
        );
    }

    public static UserRegister createUserRegister(String email, String password) {
        return new UserRegister(email,
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
