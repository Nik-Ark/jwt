package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.service.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/logout")
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
        log.info("START endpoint logout, principal: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        logoutService.logout();

        log.info("END endpoint logout, user with email: {}, logged out", email);
    }
}
