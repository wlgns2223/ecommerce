package com.ecommerce.adapter.persistence;

import com.ecommerce.application.auth.required.RefreshStore;
import com.ecommerce.application.auth.required.RefreshTokenRepository;
import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.dto.request.TokenCreateRequest;
import com.ecommerce.domain.auth.entity.RefreshToken;
import com.ecommerce.domain.auth.exception.InvalidTokenException;
import com.ecommerce.domain.auth.required.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class RefreshStoreAdapter implements RefreshStore {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHasher tokenHasher;

    @Override
    @Transactional
    public void save(IssuedToken refreshToken, Long userId) {
        RefreshToken entity = RefreshToken.issue(TokenCreateRequest.builder()
                .rawToken(refreshToken.token())
                .expiresAt(LocalDateTime.ofInstant(refreshToken.expiresAt(), ZoneId.systemDefault()))
                .userId(userId)
                .deviceId(refreshToken.deviceId())
                .build(), tokenHasher);

        refreshTokenRepository.save(entity);

    }

    @Override
    @Transactional
    public RefreshToken validate(String rawRefreshToken) {
        String hashed = tokenHasher.hash(rawRefreshToken);

        return refreshTokenRepository.findByTokenHashAndExpiresAtAfterForUpdate(hashed, LocalDateTime.now())
                .orElseThrow(() -> new InvalidTokenException("토큰을 찾을 수 없습니다."));
    }
}
