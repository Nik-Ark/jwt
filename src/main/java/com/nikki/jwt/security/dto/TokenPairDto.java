package com.nikki.jwt.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TokenPairDto {

    private final String token;
    private final String refreshToken;
}
