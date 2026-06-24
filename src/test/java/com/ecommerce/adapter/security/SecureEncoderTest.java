package com.ecommerce.adapter.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecureEncoderTest {

    @Test
    @DisplayName("인코딩된 패스워드가 일치한다")
    void matches() {
        // given
        SecureEncoder encoder = new SecureEncoder();
        String raw = "password";

        // when
        String hashedPassword = encoder.encode(raw);

        // then
        assertThat(encoder.matches(raw, hashedPassword)).isTrue();

    }

}