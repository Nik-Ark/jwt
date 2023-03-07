package com.nikki.jwt.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
