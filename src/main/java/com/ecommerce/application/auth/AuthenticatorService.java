package com.ecommerce.application.auth;

import com.ecommerce.application.auth.provided.Authenticator;
import com.ecommerce.application.auth.required.RefreshStore;
import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.auth.entity.RefreshToken;
import com.ecommerce.domain.auth.enums.TokenType;
import com.ecommerce.domain.auth.exception.InvalidTokenException;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.exception.UnauthorizedException;
import com.ecommerce.domain.user.required.Encoder;
import com.ecommerce.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticatorService implements Authenticator {

    private final UserRepository userRepository;
    private final Encoder encoder;
    private final TokenProvider tokenProvider;
    private final RefreshStore refreshStore;

    @Override
    @Transactional
    public TokenResult login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(Email.of(loginRequest.email()))
                .orElseThrow(() -> new UnauthorizedException("잘못된 이메일 및 패스워드입니다."));

        if (!encoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("잘못된 이메일 및 패스워드입니다.");
        }

        return issueAccessAndRefreshToken(loginRequest, user);
    }

    private TokenResult issueAccessAndRefreshToken(UserLoginRequest loginRequest, User user) {
        Instant now = Instant.now();
        IssuedToken accessToken = tokenProvider.createAccessToken(new Email(loginRequest.email()), loginRequest.deviceId(), now);
        IssuedToken rawRefreshToken = tokenProvider.createRefreshToken(new Email(loginRequest.email()), loginRequest.deviceId(), now);
        refreshStore.save(rawRefreshToken, user.getId());
        return new TokenResult(accessToken, rawRefreshToken);

    }

    @Override
    @Transactional
    public TokenResult renew(String rawRefreshToken) {
        IssuedToken parsed = tokenProvider.parseToken(rawRefreshToken);
        if (TokenType.ACCESS.equals(parsed.tokenType())) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }

        RefreshToken refreshToken = refreshStore.validate(rawRefreshToken);
        refreshToken.revoke(LocalDateTime.now());

        Instant now = Instant.now();
        IssuedToken issuedAccessToken = tokenProvider.createAccessToken(parsed.email(), parsed.roles(), parsed.deviceId(), now);
        IssuedToken issuedRefreshToken = tokenProvider.createRefreshToken(parsed.email(), parsed.roles(), parsed.deviceId(), now);

        refreshStore.save(issuedRefreshToken, refreshToken.getUserId());

        return new TokenResult(issuedAccessToken, issuedRefreshToken);
    }
}
