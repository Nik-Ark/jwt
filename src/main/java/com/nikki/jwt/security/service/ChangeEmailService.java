package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return changeAdminEmailByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewEmail()
        );
    }

    public SecurityUserResponse changeAdminEmailSuperior(ChangeEmailRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        return changeAdminEmailByEmail(targetUserEmail, request.getNewEmail());
    }

    private SecurityUserResponse changeAdminEmailByEmail(String targetUserEmail, String newEmail) {
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
        adminService.save(admin);
        return tokenPairService.createAndSaveTokenPair(securityUser);
    }

    public SecurityUserResponse changeManagerEmailSelf(ChangeEmailRequest request) {
        validateRequestAndIssuerPassword(request);
        return changeManagerEmailByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewEmail()
        );
    }

    public SecurityUserResponse changeManagerEmailSuperior(ChangeEmailRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        return changeManagerEmailByEmail(targetUserEmail, request.getNewEmail());
    }

    private SecurityUserResponse changeManagerEmailByEmail(String targetUserEmail, String newEmail) {
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
        managerService.save(manager);
        return tokenPairService.createAndSaveTokenPair(securityUser);
    }

    private void validateRequestAndIssuerPassword(ChangeEmailRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
    }
}
