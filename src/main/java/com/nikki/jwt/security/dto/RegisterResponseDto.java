package com.nikki.jwt.security.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class RegisterResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String[] roles;
    private String accessToken;
    private String refreshToken;

    @Override
    public String toString() {
        return "RegisterResponseDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
