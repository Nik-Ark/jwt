package com.nikki.jwt.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "authorities")
@Getter
@Setter
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "authorities")
    Set<SecurityUser> securityUsers;

    @Override
    public String toString() {
        return "Authority: {" +
                "name: '" + name + '\'' +
                '}';
    }
}
