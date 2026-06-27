package com.ecommerce.application.auth.required;

import com.ecommerce.domain.auth.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.tokenHash = :hash AND rt.expiresAt > :now")
    Optional<RefreshToken> findByTokenHashAndExpiresAtAfterForUpdate(@Param("hash") String hash, @Param("now") LocalDateTime now);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
