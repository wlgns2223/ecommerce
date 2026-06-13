package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.common.BaseEntity;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.required.PasswordEncoder;
import com.ecommerce.domain.user.vo.Email;
import com.ecommerce.domain.user.vo.Nickname;
import com.ecommerce.domain.user.vo.Phone;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
public class User extends BaseEntity {

    @Embedded
    @NaturalId
    Email email;

    @Column(nullable = false)
    String password;

    @Embedded
    Nickname nickname;

    @Embedded
    Phone phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    UserDetail detail;

    @Builder
    private User(Email email, String password, Nickname nickname, Phone phone, UserStatus status, UserDetail detail) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.status = status;
        this.detail = detail;
    }

    public static User create(UserRegisterRequest request, PasswordEncoder passwordEncoder) {
        String rawPassword = requireNonNull(request.password());
        validatePassword(rawPassword);

        User user = new User();
        user.email = new Email(requireNonNull(request.email()));
        user.password = passwordEncoder.encode(rawPassword);
        user.nickname = new Nickname(requireNonNull(request.nickname()));
        user.phone = new Phone(requireNonNull(request.phone()));
        user.status = UserStatus.ACTIVE;
        user.detail = UserDetail.createUserDetail(request.detailCreateRequest());
        return user;
    }

    private static void validatePassword(String raw) {
        if (raw.length() < 4 || raw.length() > 64) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }
}
