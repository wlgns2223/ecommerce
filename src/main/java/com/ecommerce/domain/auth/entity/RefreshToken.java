package com.ecommerce.domain.auth.entity;

import com.ecommerce.domain.auth.dto.request.TokenCreateRequest;
import com.ecommerce.domain.auth.required.TokenHasher;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "refresh_tokens")
@SQLDelete(sql = "UPDATE refresh_tokens SET revoked_at = NOW() WHERE id = ?")
@SQLRestriction("revoked_at IS NULL")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false, unique = true)
    String tokenHash;

    @Column(nullable = false)
    String deviceId;

    @Column(nullable = false)
    LocalDateTime expiresAt;

    @Column
    LocalDateTime revokedAt;

    public static RefreshToken issue(TokenCreateRequest request, TokenHasher hasher) {

        RefreshToken token = new RefreshToken();
        token.userId = requireNonNull(request.userId());
        token.expiresAt = requireNonNull(request.expiresAt());
        token.deviceId = requireNonNull(request.deviceId());
        token.tokenHash = hasher.hash(requireNonNull(request.rawToken()));
        return token;
    }

    public void revoke(LocalDateTime now) {
        revokedAt = now;
    }
}
