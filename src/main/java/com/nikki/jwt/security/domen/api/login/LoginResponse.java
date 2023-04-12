package com.nikki.jwt.security.domen.api.login;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class LoginResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String[] roles;
    private String accessToken;
    private String refreshToken;

    @Override
    public String toString() {
        return "LoginResponse: {" +
                " firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", email: '" + email + '\'' +
                ", roles: " + Arrays.toString(roles) +
                " " + '}';
    }
}
