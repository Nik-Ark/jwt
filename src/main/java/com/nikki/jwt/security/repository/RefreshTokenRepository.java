package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteRefreshTokensBySecurityUserEmail(String email);
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Query("delete from RefreshToken rt where rt.expiresAt <= ?1")
    void deleteAllExpiredRefreshTokensSince(Date now);
}
