package com.nikki.jwt.security.domen.response.exception;

import com.nikki.jwt.security.domen.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponse> handleHandledException(HandledException ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                , ex.getHttpStatus()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        System.out.println("From Exception Handler:");
        System.out.println("Authentication exception: " + ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
                , HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        ex.printStackTrace();
        System.out.println("internal server error: " + ex.toString());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("Internal Server Error")
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
