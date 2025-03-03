package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.password.ChangePasswordRequest;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService {
    private final AdminService adminService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;



    public AdminResponse changeAdminPasswordSelf(ChangePasswordRequest request) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public AdminResponse changeAdminPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        AdminResponse adminResponse;
        try {
            adminResponse = changeAdminPasswordByEmail(targetUserEmail, request.getNewPassword());
        } catch (UsernameNotFoundException exception) {
            log.error("Admin with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .cause(exception.getCause())
                    .build();
        }
        return adminResponse;
    }

    private AdminResponse changeAdminPasswordByEmail(String targetUserEmail, String newPassword) {
        Admin admin = adminService.findAdminByEmail(targetUserEmail);
        SecurityUser securityUser = admin.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        adminService.save(admin);
        return adminService.mapToAdminResponse(admin);
    }

    public ManagerResponse changeManagerPasswordSelf(ChangePasswordRequest request) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        return changeManagerPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public ManagerResponse changeManagerPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        ManagerResponse managerResponse;
        try {
            managerResponse = changeManagerPasswordByEmail(targetUserEmail, request.getNewPassword());
        } catch (UsernameNotFoundException exception) {
            log.error("Manager with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .cause(exception.getCause())
                    .build();
        }
        return managerResponse;
    }

    private ManagerResponse changeManagerPasswordByEmail(String targetUserEmail, String newPassword) {
        Manager manager = managerService.findManagerByEmail(targetUserEmail);
        SecurityUser securityUser = manager.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        managerService.save(manager);
        return managerService.mapToManagerResponse(manager);
    }

    public ClientResponse changeClientPasswordSelf(ChangePasswordRequest request) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        return changeClientPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public ClientResponse changeClientPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        ClientResponse clientResponse;
        try {
            clientResponse = changeClientPasswordByEmail(targetUserEmail, request.getNewPassword());
        } catch (UsernameNotFoundException exception) {
            log.error("Client with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .cause(exception.getCause())
                    .build();
        }
        return clientResponse;
    }

    private ClientResponse changeClientPasswordByEmail(String targetUserEmail, String newPassword) {
        Client client = clientService.findClientByEmail(targetUserEmail);
        SecurityUser securityUser = client.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        clientService.save(client);
        return clientService.mapToClientResponse(client);
    }
}
