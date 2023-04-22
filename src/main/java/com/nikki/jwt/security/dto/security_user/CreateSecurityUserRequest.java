package com.nikki.jwt.security.dto.security_user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CreateSecurityUserRequest {
    private String email;
    private String password;
}
