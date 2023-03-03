package com.nikki.jwt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "jwt")
public record JwtProperties(
        String secret
) {}
