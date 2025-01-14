package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {

    boolean existsByEmail(String email);

    Optional<SecurityUser> findByEmail(String email);

    void deleteSecurityUserByEmail(String email);
}
