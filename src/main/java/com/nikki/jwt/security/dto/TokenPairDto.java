package com.nikki.jwt.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPairDto {

    private final String token;
    private final String refreshToken;
}
