package com.ecommerce.config;

import com.ecommerce.domain.user.UserFixture;
import com.ecommerce.domain.user.required.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return UserFixture.createPasswordEncoder();
    }
}
