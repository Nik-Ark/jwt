package com.nikki.jwt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "db")
public record DataSourceProperties(
        String url, int port, String name, boolean allowPKRetrieval, boolean useSSL, String username, String password
) {}
