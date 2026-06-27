package com.ecommerce.application.auth.required;

import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.entity.RefreshToken;

public interface RefreshStore {
    void save(IssuedToken refreshToken, Long userId);

    RefreshToken validate(String rawRefreshToken);

}
