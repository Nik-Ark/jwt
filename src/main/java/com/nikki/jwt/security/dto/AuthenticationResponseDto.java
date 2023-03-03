package com.nikki.jwt.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AuthenticationResponseDto {

    private String token;
}
