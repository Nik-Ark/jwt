package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.password.ChangePasswordRequest;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.manager.ChangeManagerInfoRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.service.ChangeEmailService;
import com.nikki.jwt.security.service.ChangePasswordService;
import com.nikki.jwt.security.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/manager")
public class ProfileManagerController {

    private final ManagerService managerService;
    private final ChangeEmailService changeEmailService;
    private final ChangePasswordService changePasswordService;

    @GetMapping
    public ResponseEntity<ManagerResponse> getManagerProfile() {
        log.info("START endpoint '/profile/manager' (Get), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        ManagerResponse managerResponse = managerService.getManagerInfoSelf();

        log.info("END endpoint '/profile/manager' (Get), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<ManagerResponse> changeManagerInfoProfile(@RequestBody ChangeManagerInfoRequest request) {
        log.info("START endpoint '/profile/manager/info' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ManagerResponse managerResponse = managerService.changeManagerInfoSelf(request);

        log.info("END endpoint '/profile/manager/info' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<SecurityUserResponse> changeManagerEmailProfile(@RequestBody ChangeEmailRequest request) {
        log.info("START endpoint '/profile/manager/email' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        SecurityUserResponse securityUserResponse = changeEmailService.changeManagerEmailSelf(request);

        log.info("END endpoint '/profile/manager/email' (Put), response: {}.", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<ManagerResponse> changeManagerPasswordProfile(@RequestBody ChangePasswordRequest request) {
        log.info("START endpoint '/profile/manager/password' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ManagerResponse managerResponse = changePasswordService.changeManagerPasswordSelf(request);

        log.info("END endpoint '/profile/manager/password' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }
}
