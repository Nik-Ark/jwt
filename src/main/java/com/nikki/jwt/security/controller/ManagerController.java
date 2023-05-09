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
@RequestMapping("api/v1/managers")
public class ManagerController {

    private final ManagerService managerService;
    private final ChangeEmailService changeEmailService;
    private final ChangePasswordService changePasswordService;

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint '/managers' (Get), principal: {}, request param (count): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<ManagerResponse> managers = managerService.getManagers(count);

        log.info("END endpoint '/managers' (Get), response containing {} managers.", managers.size());
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@RequestBody CreateManagerRequest request) {
        log.info("START endpoint '/managers' (Post), principal: {}, request: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ManagerResponse managerResponse = managerService.createManager(request);

        log.info("END endpoint '/managers' (Post), created: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void deleteManager(@RequestBody DeleteRequest request, @RequestParam @NotNull @NotBlank String email) {
        log.info("START endpoint '/managers/delete' (Post), principal: {}, request param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), email);

        managerService.removeManagerSuperior(request, email);

        log.info("END endpoint '/managers/delete' (Post), Manager with email: {} deleted.", email);
    }

    @GetMapping("/{email}/info")
    public ResponseEntity<ManagerResponse> getManagerInfo(@NotNull @NotBlank @PathVariable String email) {
        log.info(
                "START endpoint '/managers/{}/info' (Get), principal: {}",
                email, SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );

        ManagerResponse managerResponse = managerService.getManagerInfoSuperior(email);

        log.info("END endpoint '/managers/{}/info' (Get), response: {}.", email, managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<ManagerResponse> changeManagerInfo(
            @RequestBody ChangeManagerInfoRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/managers/info' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = managerService.changeManagerInfoSuperior(request, email);

        log.info("END endpoint '/managers/info' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    /*
    AFTER CHANGING OF EMAIL AND !!! PASSWORD !!! DELETE TOKENS, SO CHANGED OBJECT HAS TO REAUTHENTICATE
    */
    @PutMapping("/email")
    public ResponseEntity<ManagerResponse> changeManagerEmail(
            @RequestBody ChangeEmailRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/managers/email' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = changeEmailService.changeManagerEmailSuperior(request, email);

        log.info("END endpoint '/managers/email' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    /*
    AFTER CHANGING OF EMAIL AND !!! PASSWORD !!! DELETE TOKENS, SO CHANGED OBJECT HAS TO REAUTHENTICATE
    */
    @PutMapping("/password")
    public ResponseEntity<ManagerResponse> changeManagerPassword(
            @RequestBody ChangePasswordRequest request,
            @RequestParam @NotNull @NotBlank String email
    ) {
        log.info("START endpoint '/managers/password' (Put), principal: {}, request: {}, param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, email);

        ManagerResponse managerResponse = changePasswordService.changeManagerPasswordSuperior(request, email);

        log.info("END endpoint '/managers/password' (Put), response: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }
}
