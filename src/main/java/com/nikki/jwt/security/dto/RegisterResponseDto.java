package com.nikki.jwt.security.dto;

import lombok.Builder;

@Builder
public class RegisterResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String[] roles;
    private String accessToken;
    private String refreshToken;
}
