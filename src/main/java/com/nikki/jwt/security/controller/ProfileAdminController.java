package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile/admin")
public class ProfileAdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<AdminResponse> getAdminProfile() {
        log.info("START endpoint profile/admin (Get), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        AdminResponse adminResponse = adminService.getAdminProfile();

        log.info("END endpoint profile/admin (Get), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<AdminResponse> changeAdminInfo() {
        return null;
    }
}
