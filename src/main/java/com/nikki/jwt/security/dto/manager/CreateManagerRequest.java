package com.nikki.jwt.security.dto.manager;

import com.nikki.jwt.security.domen.constant.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateManagerRequest {

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String email;

    @NotNull
    @NotBlank(message = "password is required")
    @Pattern(regexp = RegExp.password, message = "invalid password format")
    private String password;

    @NotNull
    @NotBlank(message = "firstName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String firstName;

    @NotNull
    @NotBlank(message = "lastName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String lastName;

    private String phoneNumber;

    @Override
    public String toString() {
        return "CreateManagerRequest: {" +
                " email: '" + email + '\'' +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                " " + '}';
    }
}