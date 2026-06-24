package com.ecommerce.application.auth;

import com.ecommerce.application.auth.provided.Authenticator;
import com.ecommerce.application.auth.required.RefreshTokenRepository;
import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.auth.required.IssuedToken;
import com.ecommerce.domain.auth.required.TokenHasher;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.exception.UnauthorizedException;
import com.ecommerce.domain.user.required.Encoder;
import com.ecommerce.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticatorService implements Authenticator {

    private final UserRepository userRepository;
    private final Encoder encoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHasher tokenHasher;

    @Override
    public String login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(Email.of(loginRequest.email()))
                .orElseThrow(() -> new UnauthorizedException("잘못된 이메일 및 패스워드입니다."));

        if(!encoder.matches(loginRequest.password(),user.getPassword())){
            throw new UnauthorizedException("잘못된 이메일 및 패스워드입니다.");
        }

        Instant now = Instant.now();
        IssuedToken accessToken = tokenProvider.createAccessToken(loginRequest.email(),loginRequest.deviceId(), now);
        IssuedToken rawRefreshToken = tokenProvider.createRefreshToken(loginRequest.email(),loginRequest.deviceId(), now);

        return null;
    }

}
