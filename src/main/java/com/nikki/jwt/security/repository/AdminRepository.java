package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {

    boolean existsByEmail(String email);
}
