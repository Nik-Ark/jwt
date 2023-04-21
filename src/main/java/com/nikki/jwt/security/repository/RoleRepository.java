package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository <Role, Long> {

    boolean existsByName(String name);
    Optional<Role> findByName(String roleName);
}
