package com.nikki.jwt.security.domen.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String message;
}
