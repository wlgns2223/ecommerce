package com.ecommerce.domain.auth.entity;

import com.ecommerce.domain.auth.dto.TokenCreateRequest;
import com.ecommerce.domain.auth.required.TokenHasher;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "refresh_tokens")
@SQLDelete(sql = "UPDATE refresh_tokens SET revoked_at = NOW() WHERE id = ?")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false,unique = true)
    String tokenHash;

    @Column(nullable = false)
    String deviceId;

    @Column(nullable = false)
    LocalDateTime expiresAt;

    @Column
    LocalDateTime revokedAt;

    public static RefreshToken create(TokenCreateRequest request, TokenHasher hasher){

        RefreshToken token = new RefreshToken();
        token.userId = requireNonNull(request.userId());
        token.expiresAt = requireNonNull(request.expiresAt());
        token.deviceId = requireNonNull(request.deviceId());
        token.tokenHash = hasher.hash(requireNonNull(request.rawToken()));
        return token;
    }
}
