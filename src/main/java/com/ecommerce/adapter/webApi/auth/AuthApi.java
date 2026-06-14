package com.ecommerce.adapter.webApi.auth;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.adapter.webApi.user.mapper.UserMapper;
import com.ecommerce.application.user.provided.UserRegister;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {

    final UserRegister userRegister;
    final UserMapper userMapper;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userRegister.register(request);
        return ApiResponse.ok(userMapper.toResponse(user));
    }


}
