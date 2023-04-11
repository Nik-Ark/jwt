package com.nikki.jwt.security.util;

import com.nikki.jwt.security.domen.response.exception.HandledException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class ValidationUtil {

    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> void validationRequest(T req) {

        if (req != null) {
            Set<ConstraintViolation<T>> result = validator.validate(req);
            if (!result.isEmpty()) {
                String violations = result.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((acc, curr) -> acc + ". " + curr).orElse("");
                System.out.println("invalid json in request registration, validation errors: " + violations);
                throw HandledException.builder()
                        .message(violations)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
    }
}
