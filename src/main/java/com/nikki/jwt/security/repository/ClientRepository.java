package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String email);

    void deleteClientByEmail(String email);
}
