package com.nikki.jwt.security.dto.security_user;

import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String[] roles;
    private String accessToken;
    private String refreshToken;

    @Override
    public String toString() {
        return "SecurityUserResponse: {" +
                " email: '" + email + '\'' +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", roles: " + Arrays.toString(roles) +
                " " + '}';
    }
}
