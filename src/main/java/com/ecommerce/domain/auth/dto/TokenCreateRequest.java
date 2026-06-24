package com.ecommerce.domain.auth.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TokenCreateRequest(Long userId, String rawToken, String deviceId, LocalDateTime expiresAt) {
}
