package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChangeEmailService {

    private final SecurityUserService securityUserService;
    private final AdminService adminService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final ValidationUtil validationUtil;
    private final TokenPairService tokenPairService;

    public SecurityUserResponse changeAdminEmailSelf(ChangeEmailRequest request) {
        validateRequestAndIssuerPassword(request);
        Admin admin = changeAdminEmailByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewEmail()
        );
        SecurityUser securityUser = admin.getSecurityUser();
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public AdminResponse changeAdminEmailSuperior(ChangeEmailRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        Admin admin;
        try {
            admin = changeAdminEmailByEmail(targetUserEmail, request.getNewEmail());
        } catch (UsernameNotFoundException exception) {
            log.error("Admin with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(admin.getSecurityUser().getEmail());
        return adminService.mapToAdminResponse(admin);
    }

    private Admin changeAdminEmailByEmail(String targetUserEmail, String newEmail) {
        if (securityUserService.securityUserExistsByEmail(newEmail)) {
            log.error("nickname already exists: {}", newEmail);
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        Admin admin = adminService.findAdminByEmail(targetUserEmail);
        SecurityUser securityUser = admin.getSecurityUser();
        admin.setEmail(newEmail);
        securityUser.setEmail(newEmail);
        return adminService.save(admin);
    }

    public SecurityUserResponse changeManagerEmailSelf(ChangeEmailRequest request) {
        validateRequestAndIssuerPassword(request);
        Manager manager = changeManagerEmailByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewEmail()
        );
        SecurityUser securityUser = manager.getSecurityUser();
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public ManagerResponse changeManagerEmailSuperior(ChangeEmailRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        Manager manager;
        try {
            manager = changeManagerEmailByEmail(targetUserEmail, request.getNewEmail());
        } catch (UsernameNotFoundException exception) {
            log.error("Manager with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(manager.getSecurityUser().getEmail());
        return managerService.mapToManagerResponse(manager);
    }

    private Manager changeManagerEmailByEmail(String targetUserEmail, String newEmail) {
        if (securityUserService.securityUserExistsByEmail(newEmail)) {
            log.error("nickname already exists: {}", newEmail);
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        Manager manager = managerService.findManagerByEmail(targetUserEmail);
        SecurityUser securityUser = manager.getSecurityUser();
        manager.setEmail(newEmail);
        securityUser.setEmail(newEmail);
        return managerService.save(manager);
    }

    public SecurityUserResponse changeClientEmailSelf(ChangeEmailRequest request) {
        validateRequestAndIssuerPassword(request);
        Client client = changeClientEmailByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewEmail()
        );
        SecurityUser securityUser = client.getSecurityUser();
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public ClientResponse changeClientEmailSuperior(ChangeEmailRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        Client client;
        try {
            client = changeClientEmailByEmail(targetUserEmail, request.getNewEmail());
        } catch (UsernameNotFoundException exception) {
            log.error("Client with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(client.getSecurityUser().getEmail());
        return clientService.mapToClientResponse(client);
    }

    private Client changeClientEmailByEmail(String targetUserEmail, String newEmail) {
        if (securityUserService.securityUserExistsByEmail(newEmail)) {
            log.error("nickname already exists: {}", newEmail);
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        Client client = clientService.findClientByEmail(targetUserEmail);
        SecurityUser securityUser = client.getSecurityUser();
        client.setEmail(newEmail);
        securityUser.setEmail(newEmail);
        return clientService.save(client);
    }

    private void validateRequestAndIssuerPassword(ChangeEmailRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
    }
}
