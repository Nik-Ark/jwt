package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.confirm_email.ConfirmRegisterMailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
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

        mailSender.send(mailMessage);
        log.info("Confirming registration email sent to: {}", confirmRegisterMailMessage.getTo());
        log.info(confirmRegisterMailMessage.toString());
    }
}
