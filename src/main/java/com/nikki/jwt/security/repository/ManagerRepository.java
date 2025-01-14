package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByEmail(String email);

    Optional<Manager> findByEmail(String email);

    void deleteManagerByEmail(String email);
}
