package com.ecommerce.config;

import com.ecommerce.domain.UserFixture;
import com.ecommerce.domain.user.required.Encoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    Encoder passwordEncoder() {
        return UserFixture.createPasswordEncoder();
    }
}
