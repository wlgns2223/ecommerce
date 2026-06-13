package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.common.BaseEntity;
import com.ecommerce.domain.user.dto.UserDetailCreateRequest;
import com.ecommerce.domain.user.enums.Gender;
import com.ecommerce.domain.user.enums.TierCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "user_details")
public class UserDetail extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(nullable = false)
    LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TierCode tierCode;

    @Builder
    private UserDetail(Gender gender, LocalDate birthDate, TierCode tierCode) {
        this.gender = gender;
        this.birthDate = birthDate;
        this.tierCode = tierCode;
    }

    public static UserDetail createUserDetail(UserDetailCreateRequest request) {
        UserDetail detail = new UserDetail();
        detail.gender = request.gender();
        detail.birthDate = request.birthDate();
        detail.tierCode = TierCode.BASIC;
        return detail;
    }
}
