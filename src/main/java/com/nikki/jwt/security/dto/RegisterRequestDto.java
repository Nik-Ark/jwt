package com.nikki.jwt.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
