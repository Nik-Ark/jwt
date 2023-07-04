package com.nikki.jwt.security.dto.confirm_email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ConfirmRegisterMailMessage {

    private String to;
    private String subject;
    private String message;

    @Override
    public String toString() {
        return "ConfirmRegisterMailMessage: {" +
                " to: '" + to + '\'' +
                ", subject: '" + subject + '\'' +
                ", message: '" + message + '\'' +
                " " + '}';
    }
}
