package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        select t from Token t inner join SecurityUser su on t.securityUser.id = su.id
        where su.id = :securityUserId and (t.expired = false and t.revoked = false)
    """)
    List<Token> findAllValidTokensBySecurityUserId(Integer securityUserId);

    Optional<Token> findByToken(String token);
}
