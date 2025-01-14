package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.login.LoginRequest;
import com.nikki.jwt.security.dto.register_applicant.CreateRegisterApplicantRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    /*

        WILL RETURN SECURITY USER: SecurityUser {id: 4, email: 'user4', roles: [Role: {name: 'DEVELOPER'}]}
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        WILL RETURN: WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null]
        SecurityContextHolder.getContext().getAuthentication().getDetails();

        WILL RETURN: UsernamePasswordAuthenticationToken (WITH ALL THE DETAILS INSIDE IT)
        SecurityContextHolder.getContext().getAuthentication();

        WILL RETURN USERNAME (USER-EMAIL IN MY APPLICATION)
        SecurityContextHolder.getContext().getAuthentication().getName();

    */
    private final AuthenticationService authenticationService;



    @PostMapping("/register")
    public ResponseEntity<String> registerApplicant(@RequestBody CreateRegisterApplicantRequest request) {
        log.info("START endpoint '/register' (Post), request: {}", request);
        log.info("Security Context Was Set for Principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        authenticationService.registerApplicant(request);

        log.info("END endpoint '/register'");
        return new ResponseEntity<>("Email verification sent", HttpStatus.OK);
    }

    @PostMapping("/register-confirm/{registerApplicantId}")
    public ResponseEntity<SecurityUserResponse> confirmRegistration(
            @PathVariable @NotNull @NotBlank String registerApplicantId
    ) {
        log.info("START endpoint '/confirm-register' (Post), confirm id: {}", registerApplicantId);
        log.info("Security Context Was Set for Principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        SecurityUserResponse securityUserResponse = authenticationService.confirmRegistration(registerApplicantId);

        log.info("END endpoint '/confirm-register', response: {}", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SecurityUserResponse> login(@RequestBody LoginRequest request) {
        log.info("START endpoint '/login' (Post), request: {}", request);
        log.info("Security Context Was Set for Principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        SecurityUserResponse securityUserResponse = authenticationService.login(request);

        log.info("END endpoint '/login', response: {}", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<SecurityUserResponse> refreshToken(HttpServletRequest request) {
        log.info("START endpoint '/refresh' (Get)");
        log.info("Security Context Was Set for Principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        SecurityUserResponse securityUserResponse = authenticationService.refreshToken(request);

        log.info("END endpoint '/refresh', response: {}", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.OK);
    }
}
