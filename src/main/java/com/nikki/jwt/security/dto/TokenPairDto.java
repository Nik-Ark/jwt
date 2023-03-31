package com.nikki.jwt.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPairDto {

    private final String accessToken;
    private final String refreshToken;
}
