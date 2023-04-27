package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteTokensBySecurityUserEmail(String email);
    Optional<Token> findByToken(String token);
}
