package com.nikki.jwt.security.dto.manager;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponse {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Override
    public String toString() {
        return "ManagerResponse: {" +
                " email: '" + email + '\'' +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                " " + '}';
    }
}
