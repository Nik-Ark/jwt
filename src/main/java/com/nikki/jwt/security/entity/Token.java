package com.nikki.jwt.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "expires_at")
    private Date expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_user_id")
    private SecurityUser securityUser;

    @Override
    public String toString() {
        return "Token: {" +
                " id: " + id +
                ", securityUserId: " + securityUser.getId() +
                " " + '}';
    }
}
