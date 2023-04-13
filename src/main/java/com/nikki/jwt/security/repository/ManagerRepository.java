package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, String> {

}
