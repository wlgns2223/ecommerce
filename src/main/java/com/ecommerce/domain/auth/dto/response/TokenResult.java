package com.ecommerce.domain.auth.dto.response;

import com.ecommerce.domain.auth.dto.IssuedToken;

public record TokenResult(IssuedToken access, IssuedToken refresh) {
}
