package com.ecommerce.application.auth.provided;

import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.config.TestContainerConfiguration;
import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
@Transactional
class AuthenticatorTest {

    @Autowired
    Authenticator authenticator;

    @Autowired
    TokenProvider tokenProvider;
    
    @Test
    @DisplayName("이메일 패스워드를 통해 로그인하면 JWT토큰을 발급받는다.")
    void login() {
        // given
        UserLoginRequest request = UserFixture.createLoginRequest();

        
        // 이메일,패스워드를 받는다.
        // 검증한다. 이메일과 패스워드를 검증해 없으면 403에러
        // 성공시 JWT 토큰 반환
        
        // when
        String token = authenticator.login(request);
        
        // then
        assertThat(token).isNotNull();
//        Claims claims = tokenProvider.parseClaim(token);
//        assertThat(claims.getSubject()).isEqualTo(request.email());

    }

}