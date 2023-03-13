package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
      select t from Token t inner join SecurityUser su \s
      on t.securityUser.id = su.id \s
      where su.id = :securityUserId and t.revoked = false \s
    """)
    List<Token> findAllValidTokensBySecurityUserId(int securityUserId);

    @Query(value = """
      select t from Token t inner join SecurityUser su \s
      on t.securityUser.id = su.id where su.id = :securityUserId\s
    """)
    List<Token> findAllTokensBySecurityUserId(int securityUserId);

    Optional<Token> findByToken(String token);
}
