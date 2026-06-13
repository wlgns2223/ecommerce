package com.ecommerce.adapter.webApi.user;

import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "email.address")
    @Mapping(target = "nickname", source = "nickname.value")
    @Mapping(target = "phone", source = "phone.value")
    @Mapping(target = "password", ignore = true)
    UserResponse toResponse(User user);
}
