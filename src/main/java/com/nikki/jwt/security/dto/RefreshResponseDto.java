package com.nikki.jwt.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String[] roles;
    private String accessToken;
    private String refreshToken;
}
