package com.nikki.jwt.security.dto;

import com.nikki.jwt.security.domen.constant.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDto {

    @NotNull
    @NotBlank(message = "firstName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String firstName;

    @NotNull
    @NotBlank(message = "lastName is required")
    @Pattern(regexp = RegExp.name, message = "invalid name format")
    private String lastName;

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String email;

    @NotNull
    @NotBlank(message = "password is required")
    @Pattern(regexp = RegExp.password, message = "invalid password format")
    private String password;
}
