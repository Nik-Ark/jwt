package com.nikki.jwt.security.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPair {
    private final String accessToken;
    private final String refreshToken;
}
