package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteTokensBySecurityUserEmail(String email);
    Optional<Token> findByToken(String token);
    @Modifying
    @Query("delete from Token t where t.expiresAt <= ?1")
    void deleteAllExpiredTokensSince(Date now);
}
