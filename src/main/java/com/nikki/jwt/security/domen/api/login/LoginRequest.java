package com.nikki.jwt.security.domen.api.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
}
