package com.nikki.jwt.security.dto.password;

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
public class ChangePasswordRequest {

    @NotNull
    @NotBlank(message = "issuer of request must provide his password to perform an operation")
    private String issuerPassword;

    @NotNull
    @NotBlank(message = "new password is required")
    @Pattern(regexp = RegExp.password, message = "invalid password format")
    private String newPassword;

    @Override
    public String toString() {
        return "ChangePasswordRequest: {}";
    }
}
