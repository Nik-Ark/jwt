package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.admin.ChangeAdminInfoRequest;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.service.AdminService;
import com.nikki.jwt.security.service.ChangeEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile/admin")
public class ProfileAdminController {

    private final AdminService adminService;
    private final ChangeEmailService changeEmailService;

    @GetMapping
    public ResponseEntity<AdminResponse> getAdminProfile() {
        log.info("START endpoint profile/admin (Get), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        AdminResponse adminResponse = adminService.getAdminProfile();

        log.info("END endpoint profile/admin (Get), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<AdminResponse> changeAdminInfo(@RequestBody ChangeAdminInfoRequest request) {
        log.info("START endpoint profile/admin/info (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        AdminResponse adminResponse = adminService.changeAdminInfo(request);

        log.info("END endpoint profile/admin/info (Put), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<AdminResponse> changeAdminEmail(@RequestBody ChangeEmailRequest request) {
        log.info("START endpoint profile/admin/email (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        AdminResponse adminResponse = changeEmailService.changeAdminEmail(request);

        log.info("END endpoint profile/admin/email (Put), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }
}
