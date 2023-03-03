package com.nikki.jwt.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class RegisterRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
