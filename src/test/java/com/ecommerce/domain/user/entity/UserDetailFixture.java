package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.user.dto.UserDetailCreateRequest;
import com.ecommerce.domain.user.enums.Gender;

import java.time.LocalDate;

public class UserDetailFixture {

    public static UserDetailCreateRequest createUserDetailRequest() {
        return new UserDetailCreateRequest(Gender.MALE, LocalDate.of(2026, 1, 1));
    }
}
