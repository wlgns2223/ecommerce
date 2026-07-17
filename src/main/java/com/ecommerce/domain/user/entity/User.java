package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.shared.BaseEntity;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.enums.UserStatus;
import com.ecommerce.domain.user.required.PasswordEncoder;
import com.ecommerce.domain.user.vo.Email;
import com.ecommerce.domain.user.vo.Nickname;
import com.ecommerce.domain.user.vo.Phone;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    UserDetail detail;

    @Builder
    private User(Email email, String password, Nickname nickname, Phone phone, UserStatus status, Role role, UserDetail detail) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.status = status;
        this.detail = detail;
        this.role = role;
    }

    public static User create(UserRegisterRequest request, PasswordEncoder passwordEncoder) {
        return create(request, Role.USER, passwordEncoder);
    }

    public static User create(UserRegisterRequest request, Role role, PasswordEncoder passwordEncoder) {
        String rawPassword = requireNonNull(request.password());
        validatePassword(rawPassword);

        User user = new User();
        user.email = new Email(requireNonNull(request.email()));
        user.password = passwordEncoder.encode(rawPassword);
        user.nickname = new Nickname(requireNonNull(request.nickname()));
        user.phone = new Phone(requireNonNull(request.phone()));
        user.status = UserStatus.ACTIVE;
        user.role = role;
        user.detail = UserDetail.createUserDetail(request.detailCreateRequest());
        return user;
    }

    private static void validatePassword(String raw) {
        if (raw.length() < 4 || raw.length() > 64) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }

    public void delete(LocalDateTime now) {
        this.deletedAt = now;
    }
}
