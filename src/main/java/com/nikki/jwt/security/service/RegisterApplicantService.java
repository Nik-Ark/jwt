package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.confirm_email.ConfirmRegisterMailMessage;
import com.nikki.jwt.security.dto.register_applicant.CreateRegisterApplicantRequest;
import com.nikki.jwt.security.entity.RegisterApplicant;
import com.nikki.jwt.security.repository.RegisterApplicantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RegisterApplicantService {
    private final ValidationService validationService;
    private final EmailSenderService emailSenderService;
    private final RegisterApplicantRepository registerApplicantRepository;
    private final PasswordEncoder passwordEncoder;
    private final long REG_APPLICANT_ENTRY_LIFE_SPAN;


    public RegisterApplicantService(
            ValidationService validationService,
            EmailSenderService emailSenderService,
            RegisterApplicantRepository registerApplicantRepository,
            PasswordEncoder passwordEncoder,
            @Value("${REGISTER_APPLICANT_ENTRY_LIFE_SPAN}") long REG_APPLICANT_ENTRY_LIFE_SPAN
    ) {
        this.validationService = validationService;
        this.emailSenderService = emailSenderService;
        this.registerApplicantRepository = registerApplicantRepository;
        this.passwordEncoder = passwordEncoder;
        this.REG_APPLICANT_ENTRY_LIFE_SPAN = REG_APPLICANT_ENTRY_LIFE_SPAN;
    }



    public void createRegisterApplicant(CreateRegisterApplicantRequest request) {
        validationService.validateRequest(request);
        validationService.validateSecurityUserDoesNotExistByEmail(request.getEmail());

        RegisterApplicant applicant = RegisterApplicant.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .expiresAt(new Date(System.currentTimeMillis() + REG_APPLICANT_ENTRY_LIFE_SPAN))
                .build();

        registerApplicantRepository.save(applicant);
        log.info("Register Applicant saved: {}", applicant);

        // LOGS MADE IN SEND EMAIL
        try {
            emailSenderService.sendEmail(
                    ConfirmRegisterMailMessage.builder()
                            .to(request.getEmail())
                            .subject("Registration confirmation on adminchakra.striving.live")
                            .message(applicant.getId())
                            .build()
            );
        } catch (HandledException exception) {
            registerApplicantRepository.delete(applicant);
            log.info("Register Applicant deleted");
            throw exception;
        }
    }


    public RegisterApplicant getRegisterApplicant(String registerApplicantId) {
        Optional<RegisterApplicant> registerApplicant =
                registerApplicantRepository.findRegisterApplicantById(registerApplicantId);

        if (registerApplicant.isEmpty()) {
            log.error("Register applicant not found");
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        log.info("Register applicant found");
        log.info(registerApplicant.get().toString());

        if (registerApplicant.get().getExpiresAt().before(new Date(System.currentTimeMillis()))) {
            log.error("Register applicant lifespan expired");
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return registerApplicant.get();
    }


    public void deleteRegisterApplicantById(String registerApplicantId) {
        registerApplicantRepository.deleteRegisterApplicantById(registerApplicantId);
        log.info("Register applicant deleted");
    }


}
