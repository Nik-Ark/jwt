package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.RegisterApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface RegisterApplicantRepository extends JpaRepository<RegisterApplicant, String> {
    Optional<RegisterApplicant> findRegisterApplicantById(String id);
    void deleteRegisterApplicantById(String id);
    @Modifying
    @Query("delete from RegisterApplicant r where r.expiresAt <= ?1")
    void deleteAllExpiredSince(Date now);
}
