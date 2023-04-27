package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteRefreshTokensBySecurityUserEmail(String email);
    Optional<RefreshToken> findByToken(String token);
}
