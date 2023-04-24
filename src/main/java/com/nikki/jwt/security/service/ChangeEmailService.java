package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.email.ChangeEmailRequest;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeEmailService {

    private final SecurityUserService securityUserService;
    private final AdminService adminService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final ValidationUtil validationUtil;

    public AdminResponse changeAdminEmail(ChangeEmailRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminEmailByEmail(SecurityContextHolder.getContext().getAuthentication().getName(), request.getEmail());
    }

    public AdminResponse changeAdminEmail(ChangeEmailRequest request, String email) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminEmailByEmail(email, request.getEmail());
    }

    private AdminResponse changeAdminEmailByEmail(String oldEmail, String newEmail) {
        Admin admin = adminService.findAdminByEmail(oldEmail);
        SecurityUser securityUser = admin.getSecurityUser();

        admin.setEmail(newEmail);
        securityUser.setEmail(newEmail);

        adminService.save(admin);
        return adminService.mapToAdminResponse(admin);
    }
}
