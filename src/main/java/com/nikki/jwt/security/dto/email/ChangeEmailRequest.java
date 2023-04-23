package com.nikki.jwt.security.dto.email;

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
public class ChangeEmailRequest {

    @NotNull
    @NotBlank(message = "issuer of Put request must provide his password to perform an operation")
    private String issuerPassword;

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String email;

    @Override
    public String toString() {
        return "ChangeEmailRequest: {" +
                " email: '" + email + '\'' +
                " " + '}';
    }
}
