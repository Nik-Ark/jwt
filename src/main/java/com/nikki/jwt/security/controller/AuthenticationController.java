package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.LoginRequestDto;
import com.nikki.jwt.security.dto.RegisterRequestDto;
import com.nikki.jwt.security.dto.TokenPairDto;
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
    public ResponseEntity<TokenPairDto> register(@RequestBody RegisterRequestDto request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairDto> login(@RequestBody LoginRequestDto request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.login(request);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenPairDto> refreshToken(HttpServletRequest request) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return authenticationService.refreshToken(request);
    }
}
