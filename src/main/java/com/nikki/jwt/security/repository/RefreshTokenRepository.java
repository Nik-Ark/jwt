package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = """
      select t from RefreshToken t inner join SecurityUser su \s
      on t.securityUser.id = su.id \s
      where su.id = :securityUserId and t.revoked = false \s
    """)
    List<RefreshToken> findAllValidRefreshTokensBySecurityUserId(Long securityUserId);

    @Modifying
    @Query("delete from RefreshToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

    Optional<RefreshToken> findByToken(String token);
}
