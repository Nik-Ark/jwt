package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.domen.api.RefreshResponse;
import com.nikki.jwt.security.domen.api.login.LoginRequest;
import com.nikki.jwt.security.domen.api.login.LoginResponse;
import com.nikki.jwt.security.domen.api.register.RegisterRequest;
import com.nikki.jwt.security.domen.api.register.RegisterResponse;
import com.nikki.jwt.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        log.info("START endpoint register, request: {}", request);
        log.trace("UsernamePasswordAuthenticationToken in SecurityContext: {}",
                SecurityContextHolder.getContext().getAuthentication());

        ResponseEntity<RegisterResponse> response = authenticationService.register(request);

        log.info("END endpoint register, response: {}", response);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("START endpoint login, request: {}", request);
        log.trace("UsernamePasswordAuthenticationToken in SecurityContext: {}",
                SecurityContextHolder.getContext().getAuthentication());

        ResponseEntity<LoginResponse> response = authenticationService.login(request);

        log.info("END endpoint login, response: {}", response);
        return response;
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(HttpServletRequest request) {
        log.info("START endpoint refresh, request: {}", request);
        log.trace("UsernamePasswordAuthenticationToken in SecurityContext: {}",
                SecurityContextHolder.getContext().getAuthentication());

        ResponseEntity<RefreshResponse> response = authenticationService.refreshToken(request);

        log.info("END endpoint refresh, response: {}", response);
        return response;
    }
}
