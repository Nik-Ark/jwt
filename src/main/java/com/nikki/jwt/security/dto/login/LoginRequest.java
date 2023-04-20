package com.nikki.jwt.security.dto.login;

import com.nikki.jwt.security.domen.constant.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String email;

    @NotNull
    @NotBlank(message = "password is required")
    @Pattern(regexp = RegExp.password, message = "invalid password format")
    private String password;

    @Override
    public String toString() {
        return "LoginRequest: {" +
                " email: '" + email + '\'' +
                " " + '}';
    }
}
