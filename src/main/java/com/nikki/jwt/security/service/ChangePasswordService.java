package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.ChangePasswordRequest;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService {

    private final SecurityUserService securityUserService;
    private final AdminService adminService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    public AdminResponse changeAdminPasswordSelf(ChangePasswordRequest request) {
        validateRequestAndIssuerPassword(request);
        return changeAdminPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public AdminResponse changeAdminPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        return changeAdminPasswordByEmail(targetUserEmail, request.getNewPassword());
    }

    private AdminResponse changeAdminPasswordByEmail(String targetUserEmail, String newPassword) {
        Admin admin = adminService.findAdminByEmail(targetUserEmail);
        SecurityUser securityUser = admin.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        adminService.save(admin);
        return adminService.mapToAdminResponse(admin);
    }

    public ManagerResponse changeManagerPasswordSelf(ChangePasswordRequest request) {
        validateRequestAndIssuerPassword(request);
        return changeManagerPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public ManagerResponse changeManagerPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        return changeManagerPasswordByEmail(targetUserEmail, request.getNewPassword());
    }

    private ManagerResponse changeManagerPasswordByEmail(String targetUserEmail, String newPassword) {
        Manager manager = managerService.findManagerByEmail(targetUserEmail);
        SecurityUser securityUser = manager.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        managerService.save(manager);
        return managerService.mapToManagerResponse(manager);
    }

    private void validateRequestAndIssuerPassword(ChangePasswordRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
    }
}
