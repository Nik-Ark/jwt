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
    private String[] roles;
    private String accessToken;
    private String refreshToken;

    @Override
    public String toString() {
        return "SecurityUserResponse: {" +
                ", email: '" + email + '\'' +
                ", roles: " + Arrays.toString(roles) +
                " " + '}';
    }
}
