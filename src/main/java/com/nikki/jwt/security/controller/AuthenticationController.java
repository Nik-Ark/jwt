package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.domen.api.RefreshResponse;
import com.nikki.jwt.security.domen.api.login.LoginRequest;
import com.nikki.jwt.security.domen.api.login.LoginResponse;
import com.nikki.jwt.security.domen.api.register.RegisterRequest;
import com.nikki.jwt.security.domen.api.register.RegisterResponse;
import com.nikki.jwt.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.login(request);
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(HttpServletRequest request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.refreshToken(request);
    }
}
