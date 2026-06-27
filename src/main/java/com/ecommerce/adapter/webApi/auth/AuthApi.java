package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.adapter.webApi.user.mapper.UserMapper;
import com.ecommerce.application.auth.provided.Authenticator;
import com.ecommerce.application.user.provided.UserRegister;
import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    final UserRegister userRegister;
    final UserMapper userMapper;
    final Authenticator authenticator;

    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userRegister.register(request);
        return ApiResponse.ok(userMapper.toResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
        TokenResult result = authenticator.login(request);

        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_COOKIE, result.access().token())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.between(Instant.now(), result.access().expiresAt()))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_COOKIE, result.refresh().token())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.between(Instant.now(), result.refresh().expiresAt()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }


}
