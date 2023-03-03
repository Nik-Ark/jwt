package com.nikki.jwt.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    Set<SecurityUser> securityUsers;

    @Override
    public String toString() {
        return "Role: {" +
                "name: '" + name + '\'' +
                '}';
    }
}
