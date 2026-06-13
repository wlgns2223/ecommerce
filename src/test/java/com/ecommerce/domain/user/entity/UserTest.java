package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.required.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void create() {
        String password = "password";
        UserRegisterRequest request = UserFixture.createUserRegisterRequest("test@gmail.com", password);
        PasswordEncoder encoder = UserFixture.createPasswordEncoder();
        User user = User.create(request, encoder);

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(encoder.matches(password, user.getPassword())).isTrue();
    }
}