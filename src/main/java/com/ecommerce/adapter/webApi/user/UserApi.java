package com.ecommerce.adapter.webApi.user;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.application.provided.UserRegister;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserApi {

    final UserRegister userRegister;
    final UserMapper userMapper;

    @PostMapping
    ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userRegister.register(request);
        return ApiResponse.ok(userMapper.toResponse(user));
    }


}
