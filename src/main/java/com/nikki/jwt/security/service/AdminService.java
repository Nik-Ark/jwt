package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.admin.ChangeAdminInfoRequest;
import com.nikki.jwt.security.dto.admin.CreateAdminRequest;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final SecurityUserService securityUserService;
    private final ValidationService validationService;



    public AdminResponse createAdmin(CreateAdminRequest request) {
        validationService.validateRequest(request);
        validationService.validateSecurityUserDoesNotExistByEmail(request.getEmail());
        SecurityUser securityUser = securityUserService.createSecurityUser(request, ROLE.ADMIN.name());
        Admin admin = saveAdmin(request, securityUser);
        return mapToAdminResponse(admin);
    }

    private Admin saveAdmin(CreateAdminRequest request, SecurityUser securityUser) {
        Admin admin = Admin.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .securityUser(securityUser)
                .build();
        return save(admin);
    }

    public Admin save(Admin admin) {
        log.info("Admin saved: {}", admin);
        return adminRepository.save(admin);
    }

    public void removeAdminSelf() {
        removeAdminByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void removeAdminSuperior(String targetUserEmail) {
        removeAdminByEmail(targetUserEmail);
    }

    private void removeAdminByEmail(String email) {
        adminRepository.deleteAdminByEmail(email);
        securityUserService.deleteSecurityUserByEmail(email);
    }

    public boolean adminExistsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Admin with email: " + email + " not found")
        );
    }

    public AdminResponse getAdminInfoSelf() {
        return getAdminProfileByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public AdminResponse getAdminInfoSuperior(String targetUserEmail) {
        AdminResponse adminResponse;
        try {
            adminResponse = getAdminProfileByEmail(targetUserEmail);
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

    private AdminResponse getAdminProfileByEmail(String email) {
        Admin admin = findAdminByEmail(email);
        return mapToAdminResponse(admin);
    }

    public AdminResponse changeAdminInfoSelf(ChangeAdminInfoRequest request) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminInfoByEmail(request, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public AdminResponse changeAdminInfoSuperior(ChangeAdminInfoRequest request, String targetUserEmail) {
        validationService.validateRequest(request);
        validationService.validateIssuerPassword(request.getIssuerPassword());
        AdminResponse adminResponse;
        try {
            adminResponse = changeAdminInfoByEmail(request, targetUserEmail);
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

    private AdminResponse changeAdminInfoByEmail(ChangeAdminInfoRequest request, String targetUserEmail) {
        Admin admin = findAdminByEmail(targetUserEmail);
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPhoneNumber(request.getPhoneNumber() == null ? admin.getPhoneNumber() : request.getPhoneNumber());
        adminRepository.save(admin);
        return mapToAdminResponse(admin);
    }

    public AdminResponse mapToAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .email(admin.getEmail())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }
}
