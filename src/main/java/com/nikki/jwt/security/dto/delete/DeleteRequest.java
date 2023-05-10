package com.nikki.jwt.security.dto.delete;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequest {

    @NotNull
    @NotBlank(message = "issuer of request must provide his password to perform an operation")
    private String issuerPassword;

    @Override
    public String toString() {
        return "DeleteRequest: {}";
    }
}
