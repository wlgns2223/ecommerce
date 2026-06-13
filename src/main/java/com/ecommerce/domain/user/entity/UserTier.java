package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.common.BaseEntity;
import com.ecommerce.domain.user.enums.TierCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "user_tiers")
public class UserTier extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NaturalId
    TierCode tierCode;

    @Column(nullable = false)
    String tierName;
}
