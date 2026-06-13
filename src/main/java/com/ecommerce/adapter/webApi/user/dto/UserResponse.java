package com.ecommerce.adapter.webApi.user.dto;

import com.ecommerce.domain.user.entity.UserDetail;
import com.ecommerce.domain.user.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String password,
        String nickname,
        String phone,
        UserStatus status,
        UserDetail detail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
