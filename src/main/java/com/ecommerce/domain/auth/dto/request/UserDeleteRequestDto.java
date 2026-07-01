package com.ecommerce.domain.auth.dto.request;

import lombok.NonNull;

public record UserDeleteRequestDto(
        @NonNull Long userId,
        @NonNull String password
) {
}
