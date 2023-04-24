package com.nikki.jwt.security.dto.security_user;

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
public abstract class CreateSecurityUserRequest {
    private String email;
    private String password;
}
