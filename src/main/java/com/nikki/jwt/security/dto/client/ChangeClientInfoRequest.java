package com.nikki.jwt.security.dto.client;

import com.nikki.jwt.security.api.regexp.RegExp;
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
public class ChangeClientInfoRequest {

    @NotNull
    @NotBlank(message = "issuer of request must provide his password to perform an operation")
    private String issuerPassword;

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
        return "ChangeClientInfoRequest: {" +
                " firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                ", city: '" + city + '\'' +
                " " + '}';
    }
}