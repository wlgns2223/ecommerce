package com.ecommerce.domain.user.dto;

import com.ecommerce.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserDetailCreateRequest(
        @NotBlank
        Gender gender,
        LocalDate birthDate
) {
}
