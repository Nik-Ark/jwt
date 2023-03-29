package com.nikki.jwt.security.dto;

import lombok.Builder;

@Builder
public class LoginResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String[] roles;
    private String accessToken;
    private String refreshToken;
}
