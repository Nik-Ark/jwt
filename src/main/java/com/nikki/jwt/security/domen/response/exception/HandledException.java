package com.nikki.jwt.security.domen.response.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HandledException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HandledException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HandledException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public HandledException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "HandledException: {" +
                " message: " + getMessage() +
                ", cause: " + getCause() +
                ", httpStatus: " + getHttpStatus() +
                " " + '}';
    }

    public static HandledExceptionBuilder builder() {
        return new HandledExceptionBuilder();
    }

    public static class HandledExceptionBuilder {
        private String message;
        private Throwable cause;
        private HttpStatus httpStatus;

        public HandledExceptionBuilder() {}

        public HandledExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public HandledExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public HandledExceptionBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public HandledException build() {
            return new HandledException(message, cause, httpStatus);
        }
    }
}
