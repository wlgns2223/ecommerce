package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.adapter.webApi.user.mapper.UserMapper;
import com.ecommerce.application.auth.provided.Authenticator;
import com.ecommerce.application.user.provided.UserAdminRegister;
import com.ecommerce.domain.auth.dto.IssuedToken;
import com.ecommerce.domain.auth.dto.request.UserDeleteRequestDto;
import com.ecommerce.domain.auth.dto.response.TokenResult;
import com.ecommerce.domain.auth.exception.InvalidTokenException;
import com.ecommerce.domain.user.dto.UserLoginRequest;
import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    final com.ecommerce.application.user.provided.UserRegister userRegister;
    final UserMapper userMapper;
    final Authenticator authenticator;

    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";

    private final UserAdminRegister userAdminRegister;

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> adminRegister(@Valid @RequestBody UserRegister request) {
        User user = userAdminRegister.registerAdmin(request);
        URI location = URI.create("/api/admin/" + user.getId());
        return ResponseEntity.created(location).body(ApiResponse.ok(userMapper.toResponse(user)));
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegister request) {
        User user = userRegister.register(request);
        return ApiResponse.ok(userMapper.toResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest request) {
        TokenResult result = authenticator.login(request);

        ResponseCookie accessCookie = createCookie(ACCESS_COOKIE, result.access());

        ResponseCookie refreshCookie = createCookie(REFRESH_COOKIE, result.refresh());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

    @PostMapping("/renew")
    public ResponseEntity<Void> renew(HttpServletRequest request) {
        String rawRefreshToken = resolveRefreshCookie(request)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 토큰입니다."));

        TokenResult result = authenticator.renew(rawRefreshToken);

        ResponseCookie accessCookie = createCookie(ACCESS_COOKIE, result.access());

        ResponseCookie refreshCookie = createCookie(REFRESH_COOKIE, result.refresh());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();

    }


    @NonNull
    private static Optional<String> resolveRefreshCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter((c) -> REFRESH_COOKIE.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }

    @NonNull
    private static ResponseCookie createCookie(String cookieName, IssuedToken result) {
        return ResponseCookie.from(cookieName, result.token())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.between(Instant.now(), result.expiresAt()))
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        resolveRefreshCookie(request).ifPresent(authenticator::logout);

        ResponseCookie access = ResponseCookie.from(ACCESS_COOKIE)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refresh = ResponseCookie.from(REFRESH_COOKIE)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteRequestDto deleteRequestDto,
                                       @AuthenticationPrincipal Long id,
                                       HttpServletRequest request) {
        resolveRefreshCookie(request)
                .ifPresent((refreshToken) -> authenticator.delete(id, deleteRequestDto, refreshToken));

        ResponseCookie access = ResponseCookie.from(ACCESS_COOKIE)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refresh = ResponseCookie.from(REFRESH_COOKIE)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .build();
    }


}
