package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.password.ChangePasswordRequest;
import com.nikki.jwt.security.dto.delete.DeleteRequest;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.manager.ChangeManagerInfoRequest;
import com.nikki.jwt.security.dto.manager.CreateManagerRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.service.ChangeEmailService;
import com.nikki.jwt.security.service.ChangePasswordService;
import com.nikki.jwt.security.service.ManagerService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final ChangeEmailService changeEmailService;
    private final ChangePasswordService changePasswordService;

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint '/manager' (Get), principal: {}, request param (count): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<ManagerResponse> managers = managerService.getManagers(count);

        log.info("END endpoint '/manager' (Get), response containing {} managers.", managers.size());
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@RequestBody CreateManagerRequest request) {
        log.info("START endpoint '/manager' (Post), principal: {}, request: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ManagerResponse managerResponse = managerService.createManager(request);

        log.info("END endpoint '/manager' (Post), created: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void deleteManager(@RequestBody DeleteRequest request, @RequestParam @NotNull @NotBlank String email) {
        log.info("START endpoint '/manager/delete' (Post), principal: {}, request param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), email);

        managerService.removeManagerSuperior(request, email);

        log.info("END endpoint '/manager/delete' (Post), Manager with email: {} deleted.", email);
    }

    @GetMapping("/info")
    public ResponseEntity<ManagerResponse> getManagerInfo(@RequestParam @NotNull @NotBlank String email) {
        log.info("START endpoint '/manager/info' (Get), principal: {}, request param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), email);

        ManagerResponse managerResponse = managerService.getManagerInfoSuperior(email);

        log.info("END endpoint '/manager/info' (Get), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<ManagerResponse> changeManagerInfo(
            @RequestBody ChangeManagerInfoRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/manager/info' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = managerService.changeManagerInfoSuperior(request, email);

        log.info("END endpoint '/manager/info' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<ManagerResponse> changeManagerEmail(
            @RequestBody ChangeEmailRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/manager/email' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = changeEmailService.changeManagerEmailSuperior(request, email);

        log.info("END endpoint '/manager/email' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<ManagerResponse> changeManagerPassword(
            @RequestBody ChangePasswordRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/manager/password' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = changePasswordService.changeManagerPasswordSuperior(request, email);

        log.info("END endpoint '/manager/password' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }
}
