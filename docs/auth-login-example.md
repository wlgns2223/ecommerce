# Auth 로그인 컨트롤러 예제 (쿠키 기반 JWT)

현재 작성된 코드(`JwtTokenProvider`, `JwtAuthFilter`, `SecurityConfig`, `TokenProvider`/`RefreshTokenStore` 포트)에 맞춘 로그인 구현 예제다.

---

## 0. 먼저 짚을 전제 — 이 프로젝트는 "쿠키 기반" JWT다

`JwtAuthFilter`는 `Authorization` 헤더가 아니라 **쿠키**에서 액세스 토큰을 읽는다.

```java
// JwtAuthFilter.java
private String resolveCookie(HttpServletRequest request) { ... }   // 쿠키에서 토큰 추출
// accessCookieName = ${spring.jwt.cookie.access-name}
```

→ 따라서 **로그인은 토큰을 응답 바디가 아니라 `Set-Cookie`로 내려줘야** 필터가 다음 요청에서 인증을 복원할 수 있다. (헤더 방식 예제와 다른 점)

관련 보안 설정(`SecurityConfig`):

- `/api/auth/**` → `permitAll` (로그인은 인증 불필요)
- `SessionCreationPolicy.STATELESS`
- CSRF는 쿠키 저장소 사용 + `/api/auth/**`는 무시

---

## 1. 레이어 배치 (헥사고날)

```
AuthApi(login)              ← adapter.webApi.auth     : HTTP 입력 + 쿠키 굽기
   └─> AuthUseCase          ← application.auth.provided: inbound 포트
         └─> AuthService    ← application.auth         : 인증·토큰발급 오케스트레이션 (core)
               ├─ UserRepository      (application.user.required)
               ├─ PasswordEncoder     (domain.user.required)
               ├─ TokenProvider       (application.auth.required)  ← JwtTokenProvider 구현
               └─ RefreshTokenStore   (application.auth.required)
```

- **토큰 발급/검증·비밀번호 대조 = application(core)**. jjwt·Spring Security·쿠키를 모른다.
- **쿠키 굽기(HTTP 전송) = adapter(컨트롤러)**. `AuthService`는 토큰 문자열만 반환한다.

---

## 2. 선행 변경 2가지

### (1) `UserRepository`에 `findByEmail` 추가

현재는 `existsByEmail`만 있다. 로그인엔 사용자 조회가 필요하다.

```java
// application/user/required/UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(Email email);
    Optional<User> findByEmail(Email email);   // ← 추가
}
```

### (2) 인증 실패 예외 (Spring Security 타입 금지)

core에서 `org.springframework.security.authentication.BadCredentialsException`을 쓰면 도메인/애플리케이션에 Spring Security가 누출된다. 기존
`DuplicatedEmailException` 스타일로 자체 예외를 만든다.

```java
// domain/user/exception/InvalidCredentialsException.java
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) { super(message); }
}
```

> 이 예외를 401로 변환하는 `@RestControllerAdvice`는 별도로 둔다(아래 8번 참고).

---

## 3. inbound 포트 + 반환 타입

```java
// application/auth/provided/AuthUseCase.java
package com.ecommerce.application.auth.provided;

public interface AuthUseCase {
    Tokens login(String email, String rawPassword);
}
```

```java
// application/auth/provided/Tokens.java
package com.ecommerce.application.auth.provided;

// 발급된 토큰 묶음 (전송 방식은 어댑터가 결정 — 여기선 쿠키)
public record Tokens(String accessToken, String refreshToken) {}
```

---

## 4. AuthService — 로그인 오케스트레이션 (core)

```java
// application/auth/AuthService.java
package com.ecommerce.application.auth;

import com.ecommerce.application.auth.provided.AuthUseCase;
import com.ecommerce.application.auth.provided.Tokens;
import com.ecommerce.application.auth.required.CacheStore;
import com.ecommerce.application.auth.required.TokenProvider;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.exception.InvalidCredentialsException;
import com.ecommerce.domain.user.required.Encoder;
import com.ecommerce.domain.user.vo.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final long refreshTokenSeconds;

    // JwtTokenProvider와 동일하게 생성자 @Value 주입
    public AuthService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       TokenProvider tokenProvider,
                       RefreshTokenStore refreshTokenStore,
                       @Value("${jwt.refresh-token-seconds}") long refreshTokenSeconds) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenStore = refreshTokenStore;
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    @Override
    @Transactional(readOnly = true)
    public Tokens login(String email, String rawPassword) {
        // 1. 사용자 조회 (없어도 "자격 증명 오류"로 통일 — 계정 존재 여부 노출 방지)
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 2. 비밀번호 대조 (도메인 PasswordEncoder 포트)
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. 권한 결정
        //    ⚠️ 현재 User 엔티티에 Role 필드가 없어 임시로 USER 고정.
        //    ADMIN 등을 지원하려면 User(또는 UserDetail)에 역할 저장을 추가하고
        //    user.getRoles() 형태로 교체해야 한다.
        List<Role> roles = List.of(Role.USER);

        // 4. 토큰 발급 (TokenProvider 포트 — jjwt를 모른다)
        Date now = new Date();
        String accessToken = tokenProvider.createAccessToken(email, roles, now);
        String refreshToken = tokenProvider.createRefreshToken(email, now);

        // 5. 리프레시 토큰 저장 (재발급 대조·로그아웃 폐기를 위해)
        refreshTokenStore.save(email, refreshToken, Duration.ofSeconds(refreshTokenSeconds));

        return new Tokens(accessToken, refreshToken);
    }
}
```

> `AuthService`는 jjwt·Spring Security·쿠키·HttpServletResponse를 전혀 모른다. 그래서
> 모킹된 4개 포트만으로 단위 테스트가 가능하다. (`now`가 `TokenProvider` 안에서 쓰이지만
> 토큰 문자열만 반환하므로 테스트는 "리프레시가 저장됐는가" 등 행위로 검증)

---

## 5. 쿠키 팩토리 (adapter) — 전송 설정을 한 곳에

컨트롤러를 얇게 유지하기 위해 쿠키 생성/만료 설정을 어댑터 컴포넌트로 분리한다.

```java
// adapter/webApi/auth/TokenCookieFactory.java
package com.ecommerce.adapter.webApi.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class TokenCookieFactory {

    private final String accessName;
    private final String refreshName;
    private final long accessSeconds;
    private final long refreshSeconds;

    public TokenCookieFactory(
            @Value("${spring.jwt.cookie.access-name}") String accessName,
            @Value("${spring.jwt.cookie.refresh-name}") String refreshName,
            @Value("${jwt.access-token-seconds}") long accessSeconds,
            @Value("${jwt.refresh-token-seconds}") long refreshSeconds) {
        this.accessName = accessName;
        this.refreshName = refreshName;
        this.accessSeconds = accessSeconds;
        this.refreshSeconds = refreshSeconds;
    }

    public ResponseCookie access(String token)  { return build(accessName, token, accessSeconds); }
    public ResponseCookie refresh(String token) { return build(refreshName, token, refreshSeconds); }

    private ResponseCookie build(String name, String token, long maxAgeSeconds) {
        return ResponseCookie.from(name, token)
                .httpOnly(true)      // JS에서 접근 불가 (XSS 방어)
                .secure(true)        // HTTPS 전용 — 로컬 HTTP 개발 시 false
                .path("/")
                .sameSite("Strict")  // 교차 출처 프론트면 "None"(+secure) 필요
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();
    }
}
```

> ⚠️ 필터는 `${spring.jwt.cookie.access-name}`을 쓰는데 토큰 만료는 `${jwt.*}`로 키 prefix가
> 달라 헷갈린다. 리프레시 쿠키명(`spring.jwt.cookie.refresh-name`)을 추가하고, 가능하면
> `jwt.*`로 prefix를 통일하는 게 좋다.

---

## 6. 로그인 컨트롤러 (요청한 핵심)

```java
// adapter/webApi/auth/AuthApi.java  (기존 register에 login 추가)
package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.adapter.webApi.auth.dto.LoginRequest;
import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.application.auth.provided.AuthUseCase;
import com.ecommerce.application.auth.provided.Tokens;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    private final AuthUseCase authUseCase;          // inbound 포트만 의존
    private final TokenCookieFactory cookieFactory;
    // ... 기존 register용 의존(userRegister, userMapper)은 그대로 ...

    @PostMapping("/login")
    public ApiResponse<Void> login(@Valid @RequestBody LoginRequest request,
                                   HttpServletResponse response) {
        // 1. 인증 + 토큰 발급은 core에 위임
        Tokens tokens = authUseCase.login(request.email(), request.password());

        // 2. 토큰을 쿠키로 내려준다 (필터가 쿠키에서 읽으므로)
        response.addHeader(HttpHeaders.SET_COOKIE, cookieFactory.access(tokens.accessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieFactory.refresh(tokens.refreshToken()).toString());

        // 3. 토큰은 쿠키에 있으므로 바디는 비워서 성공만 반환
        return ApiResponse.ok();
    }
}
```

```java
// adapter/webApi/auth/dto/LoginRequest.java
package com.ecommerce.adapter.webApi.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {}
```

> 컨트롤러의 책임: **HTTP 입력 받기 → 유스케이스 호출 → 토큰을 쿠키로 굽기.**
> 인증 로직은 한 줄(`authUseCase.login(...)`)로 위임된다. 이것이 헥사고날에서 노리는 모습이다.

---

## 7. application.yml (필요한 키)

```yaml
jwt:
  secret: ${JWT_SECRET:change-this-to-a-very-long-secret-at-least-32-bytes!!}  # 256bit 이상
  access-token-seconds: 1800       # 30분
  refresh-token-seconds: 1209600   # 14일

spring:
  jwt:
    cookie:
      access-name: ACCESS_TOKEN
      refresh-name: REFRESH_TOKEN   # 추가 필요 (필터/쿠키팩토리에서 사용)
```

---

## 8. 동작 흐름

1. `POST /api/auth/login` `{email, password}` (permitAll)
2. `AuthService` → 사용자 조회 + 비밀번호 대조 → 액세스/리프레시 토큰 발급 → 리프레시 저장
3. 컨트롤러가 두 토큰을 **HttpOnly 쿠키**로 `Set-Cookie`
4. 이후 요청마다 브라우저가 쿠키 자동 전송 → `JwtAuthFilter`가 액세스 쿠키를 읽어 `SecurityContext` 세팅
5. `/api/**`는 `hasRole("USER")` 통과 → 컨트롤러 실행

---

## 9. 주의점 · 남은 갭

- **역할(Role) 저장 부재**: `User`에 Role 필드가 없어 예제는 `List.of(Role.USER)` 고정. ADMIN을 쓰려면 역할 저장을 도입하고 `AuthService`의 roles를 교체해야
  한다.
- **`InvalidCredentialsException` → 401 매핑**: `@RestControllerAdvice`에서 이 예외를 잡아
  `ApiResponse.fail(HttpStatus.UNAUTHORIZED, ...)`로 변환한다. (안 하면 500)
- **`AuthenticationManager` 빈**: `SecurityConfig`에 선언돼 있지만 `UserDetailsService`가 없어 현재 동작 불가/미사용. 위처럼 포트 기반 자체 인증으로 가면 이
  빈은 제거해도 된다.
- **`secure(true)` / `SameSite`**: 운영(HTTPS) 기준. 로컬 HTTP 개발에선 `secure(false)`, 프론트가 다른 출처면 `SameSite=None`이 필요할 수 있다.
- **로그인 시 잘못된 이메일 형식**: `new Email(email)` VO 검증에서 `IllegalArgumentException`이 날 수 있다. 자격 증명 오류로 통일하려면 컨트롤러/어드바이스에서 함께
  처리.
