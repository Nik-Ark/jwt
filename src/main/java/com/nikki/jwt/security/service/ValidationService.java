package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private final SecurityUserRepository securityUserRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;



    public <T> void validateRequest(T request) {
        validationUtil.validationRequest(request);
    }

    public void validateSecurityUserDoesNotExistByEmail(String email) {
        if (securityUserRepository.existsByEmail(email)) {
            log.error("Nickname already exists: {}", email);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public void validateIssuerPassword(String issuerPassword) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityUser securityUser = securityUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + email + " not found")
        );
        if (!passwordEncoder.matches(issuerPassword, securityUser.getPassword())) {
            log.error("Issuer password is not valid, issuer: {}", email);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
