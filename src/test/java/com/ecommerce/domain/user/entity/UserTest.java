package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.required.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void create() {
        String password = "password";
        UserRegister request = UserFixture.createUserRegister("test@gmail.com", password);
        PasswordEncoder passwordEncoder = UserFixture.createPasswordEncoder();
        User user = User.create(request, passwordEncoder);

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();
    }
}