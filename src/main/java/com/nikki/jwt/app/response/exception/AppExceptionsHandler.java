package com.nikki.jwt.app.response.exception;

import com.nikki.jwt.app.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponse> handleHandledException(HandledException ex) {
        log.error("{}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                , ex.getHttpStatus()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication Exception after JWT Filter: {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("Authentication error")
                        .build()
                , HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex
    ) {
        log.error("MissingServletRequestParameterException: {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("Invalid request")
                        .build()
                , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("UnexpectedException: {}", ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("Internal Server Error")
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
