package com.nikki.jwt.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService {
    private final TokenPairService tokenPairService;

    public void logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(email);
        SecurityContextHolder.clearContext();
    }
}
