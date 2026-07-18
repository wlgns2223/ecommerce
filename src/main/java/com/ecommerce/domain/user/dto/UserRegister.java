package com.ecommerce.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        String phone,

        @NotNull
        UserDetailCreateRequest detailCreateRequest
) {
}
