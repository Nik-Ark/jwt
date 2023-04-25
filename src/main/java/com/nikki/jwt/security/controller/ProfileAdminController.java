package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.ChangePasswordRequest;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.admin.ChangeAdminInfoRequest;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.service.AdminService;
import com.nikki.jwt.security.service.ChangeEmailService;
import com.nikki.jwt.security.service.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/admin")
public class ProfileAdminController {

    private final AdminService adminService;
    private final ChangeEmailService changeEmailService;
    private final ChangePasswordService changePasswordService;

    @GetMapping
    public ResponseEntity<AdminResponse> getAdminProfile() {
        log.info("START endpoint profile/admin (Get), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        AdminResponse adminResponse = adminService.getAdminProfileSelf();

        log.info("END endpoint profile/admin (Get), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<AdminResponse> changeAdminInfoProfile(@RequestBody ChangeAdminInfoRequest request) {
        log.info("START endpoint profile/admin/info (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        AdminResponse adminResponse = adminService.changeAdminInfoSelf(request);

        log.info("END endpoint profile/admin/info (Put), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<SecurityUserResponse> changeAdminEmailProfile(@RequestBody ChangeEmailRequest request) {
        log.info("START endpoint profile/admin/email (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        SecurityUserResponse securityUserResponse = changeEmailService.changeAdminEmailSelf(request);

        log.info("END endpoint profile/admin/email (Put), response: {}.", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<AdminResponse> changeAdminPasswordProfile(@RequestBody ChangePasswordRequest request) {
        log.info("START endpoint profile/admin/password (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        AdminResponse adminResponse = changePasswordService.changeAdminPasswordSelf(request);

        log.info("END endpoint profile/admin/password (Put), response: {}.", adminResponse);
        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }
}
