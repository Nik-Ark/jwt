package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.ChangePasswordRequest;
import com.nikki.jwt.security.dto.client.ChangeClientInfoRequest;
import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.delete.DeleteRequest;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.service.ChangeEmailService;
import com.nikki.jwt.security.service.ChangePasswordService;
import com.nikki.jwt.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/client")
public class ProfileClientController {

    private final ClientService clientService;
    private final ChangeEmailService changeEmailService;
    private final ChangePasswordService changePasswordService;

    @GetMapping
    public ResponseEntity<ClientResponse> getClientProfile() {
        log.info("START endpoint '/profile/client' (Get), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        ClientResponse clientResponse = clientService.getClientInfoSelf();

        log.info("END endpoint '/profile/client' (Get), response: {}.", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<ClientResponse> changeClientInfoProfile(@RequestBody ChangeClientInfoRequest request) {
        log.info("START endpoint '/profile/client/info' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ClientResponse clientResponse = clientService.changeClientInfoSelf(request);

        log.info("END endpoint '/profile/client/info' (Put), response: {}.", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @PutMapping("/email")
    public ResponseEntity<SecurityUserResponse> changeClientEmailProfile(@RequestBody ChangeEmailRequest request) {
        log.info("START endpoint '/profile/client/email' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        SecurityUserResponse securityUserResponse = changeEmailService.changeClientEmailSelf(request);

        log.info("END endpoint '/profile/client/email' (Put), response: {}.", securityUserResponse);
        return new ResponseEntity<>(securityUserResponse, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<ClientResponse> changeClientPasswordProfile(@RequestBody ChangePasswordRequest request) {
        log.info("START endpoint '/profile/client/password' (Put), principal: {}, request: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ClientResponse clientResponse = changePasswordService.changeClientPasswordSelf(request);

        log.info("END endpoint '/profile/client/password' (Put), response: {}.", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void deleteClientProfile(@RequestBody DeleteRequest request) {
        log.info("START endpoint '/profile/client/delete' (Post), principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        clientService.removeClientSelf(request);

        log.info("END endpoint '/profile/client/delete' (Post), Client with email: {} deleted.",
                SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
