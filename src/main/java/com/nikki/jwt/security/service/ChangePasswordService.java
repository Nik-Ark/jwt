package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.ChangePasswordRequest;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.entity.Admin;
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

    public AdminResponse changeAdminPasswordProfile(ChangePasswordRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminPasswordByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getNewPassword()
        );
    }

    public AdminResponse changeAdminPasswordSuperior(ChangePasswordRequest request, String targetUserEmail) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminPasswordByEmail(targetUserEmail, request.getNewPassword());
    }

    private AdminResponse changeAdminPasswordByEmail(String targetUserEmail, String newPassword) {
        Admin admin = adminService.findAdminByEmail(targetUserEmail);
        SecurityUser securityUser = admin.getSecurityUser();
        securityUser.setPassword(passwordEncoder.encode(newPassword));
        adminService.save(admin);
        return adminService.mapToAdminResponse(admin);
    }
}
