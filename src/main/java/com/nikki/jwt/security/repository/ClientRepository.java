package com.nikki.jwt.security.repository;

import com.nikki.jwt.security.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {

}
