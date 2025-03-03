package com.nikki.jwt.security.util;

import com.nikki.jwt.app.response.exception.HandledException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class ValidationUtil {

    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> void validationRequest(T request) {

        if (request != null) {
            Set<ConstraintViolation<T>> result = validator.validate(request);
            if (!result.isEmpty()) {
                String violations = result.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((acc, curr) -> acc + ". " + curr).orElse("");
                log.error("invalid json in request, validation errors: {}", violations);
                throw HandledException.builder()
                        .message("Bad request")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
    }
}
