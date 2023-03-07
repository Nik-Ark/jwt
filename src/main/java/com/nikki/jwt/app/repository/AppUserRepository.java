package com.nikki.jwt.app.repository;

import com.nikki.jwt.app.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {}
