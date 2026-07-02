package com.ecommerce.application.user.provided;

import com.ecommerce.config.TestConfig;
import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.enums.TierCode;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.exception.DuplicatedEmailException;
import com.ecommerce.domain.user.required.PasswordEncoder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Profile("test")
@SpringBootTest
@Transactional
@Import(TestConfig.class)
class UserRegisterTest {

    @Autowired
    UserRegister userRegister;

    @Autowired
    EntityManager entityManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저를 생성한다.")
    void register() {
        // given
        String email = "test@gmail.com";
        String password = "password";

        // when
        User user = userRegister.register(UserFixture.createUserRegisterRequest(email, password));

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getDetail().getTierCode()).isEqualTo(TierCode.BASIC);
    }

    @Test
    @DisplayName("이메일이 중복되면 실패한다.")
    void duplicateEmailFail() {
        // given
        String email = "test@gmail.com";
        userRegister.register(UserFixture.createUserRegisterRequest(email));

        // then
        assertThatThrownBy(() -> userRegister.register(UserFixture.createUserRegisterRequest(email)))
                .isInstanceOf(DuplicatedEmailException.class);

    }

    @Test
    @DisplayName("비밀번호 규칙에 맞지 않으면 회원가입이 실패한다.")
    void wrongPassword() {
        // given
        String password = "";
        UserRegisterRequest request = UserFixture.createUserRegisterRequest("test@gmail.com", password);

        // then
        assertThatThrownBy(() -> userRegister.register(request))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("이메일 형식에 맞지 않으면 회원가입이 실패한다.")
    void wrongEmail() {
        // given
        String email = "1234";
        UserRegisterRequest request = UserFixture.createUserRegisterRequest(email);

        // then
        assertThatThrownBy(() -> userRegister.register(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}