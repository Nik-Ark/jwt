package com.nikki.jwt.app.controller;

import com.nikki.jwt.security.dto.AuthenticationResponseDto;
import com.nikki.jwt.security.dto.LoginRequestDto;
import com.nikki.jwt.security.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @RequestBody RegisterRequestDto request
    ) {
       return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @RequestBody LoginRequestDto request
    ) {
        return null;
    }
}
