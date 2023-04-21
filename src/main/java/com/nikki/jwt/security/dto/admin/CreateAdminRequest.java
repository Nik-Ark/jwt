package com.nikki.jwt.security.dto.admin;

import com.nikki.jwt.security.api.regexp.RegExp;
import com.nikki.jwt.security.dto.security_user.CreateSecurityUserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminRequest extends CreateSecurityUserRequest {

    @NotNull
    @NotBlank(message = "email is required")
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
        return "CreateAdminRequest: {" +
                " email: '" + email + '\'' +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                " " + '}';
    }
}
