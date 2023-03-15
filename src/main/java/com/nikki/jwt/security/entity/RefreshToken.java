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
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token", unique = true)
    private String token;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "security_user_id")
    private SecurityUser securityUser;

    @Override
    public String toString() {
        return "RefreshToken {" +
                "id: " + id +
                ", revoked: " + revoked +
                ", securityUserId: " + securityUser.getId() +
                '}';
    }
}
