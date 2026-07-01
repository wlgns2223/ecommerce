package com.ecommerce.adapter.webApi.user.mapper;

import com.ecommerce.adapter.webApi.user.dto.UserResponse;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.entity.UserDetail;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.vo.Email;
import com.ecommerce.domain.user.vo.Nickname;
import com.ecommerce.domain.user.vo.Phone;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T16:08:00+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.5.jar, environment: Java 17.0.19 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String email = null;
        String nickname = null;
        String phone = null;
        Long id = null;
        UserStatus status = null;
        UserDetail detail = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        email = userEmailAddress( user );
        nickname = userNicknameValue( user );
        phone = userPhoneValue( user );
        id = user.getId();
        status = user.getStatus();
        detail = user.getDetail();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();

        String password = null;

        UserResponse userResponse = new UserResponse( id, email, password, nickname, phone, status, detail, createdAt, updatedAt );

        return userResponse;
    }

    private String userEmailAddress(User user) {
        Email email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email.address();
    }

    private String userNicknameValue(User user) {
        Nickname nickname = user.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname.value();
    }

    private String userPhoneValue(User user) {
        Phone phone = user.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone.value();
    }
}
