package com.nikki.jwt.security.dto.client;

import com.nikki.jwt.security.domen.constant.RegExp;
import com.nikki.jwt.security.dto.security_user.CreateSecurityUserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest extends CreateSecurityUserRequest {

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String email;

    @NotNull
    @NotBlank(message = "firstName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String firstName;

    @NotNull
    @NotBlank(message = "lastName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String lastName;

    private String phoneNumber;

    private String city;

    @Override
    public String toString() {
        return "CreateClientRequest: {" +
                "email: '" + email + '\'' +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                ", city: '" + city + '\'' +
                " " + '}';
    }
}
