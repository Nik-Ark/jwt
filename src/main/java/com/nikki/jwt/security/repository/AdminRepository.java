package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

    void deleteAdminByEmail(String email);
}
