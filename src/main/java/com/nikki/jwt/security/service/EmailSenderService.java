package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.confirm_email.ConfirmRegisterMailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;



    public void sendEmail(ConfirmRegisterMailMessage confirmRegisterMailMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        mailMessage.setTo(confirmRegisterMailMessage.getTo());
        mailMessage.setSubject(confirmRegisterMailMessage.getSubject());
        mailMessage.setText(confirmRegisterMailMessage.getMessage());
//        mailMessage.setReplyTo("no-reply@" + mailProperties.getUsername());

        try {
            mailSender.send(mailMessage);
        } catch (MailException exception) {
            log.error("Confirmation email was not sent");
            throw HandledException.builder()
                    .message("Something went wrong")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .cause(exception.getCause())
                    .build();
        }
        log.info("Confirming registration email sent to: {}", confirmRegisterMailMessage.getTo());
        log.info(confirmRegisterMailMessage.toString());
    }
}
