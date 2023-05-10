package com.nikki.jwt.security.dto.email;

import com.nikki.jwt.security.api.regexp.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEmailRequest {

    @NotNull
    @NotBlank(message = "issuer of request must provide his password to perform an operation")
    private String issuerPassword;

    @NotNull
    @NotBlank(message = "email is required")
    @Pattern(regexp = RegExp.email, message = "invalid email format")
    private String newEmail;

    @Override
    public String toString() {
        return "ChangeEmailRequest: {" +
                " newEmail: '" + newEmail + '\'' +
                " " + '}';
    }
}
