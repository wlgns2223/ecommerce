package com.ecommerce.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserRegister(
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 4, max = 64)
        String password,
        @NotBlank
        String nickname,
        @NotBlank
        @Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해야 합니다.")
        String phone,

        @NotNull
        UserDetailCreateRequest detailCreateRequest
) {
}
