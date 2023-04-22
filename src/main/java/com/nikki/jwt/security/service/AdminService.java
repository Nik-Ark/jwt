package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.admin.ChangeAdminInfoRequest;
import com.nikki.jwt.security.dto.admin.CreateAdminRequest;
import com.nikki.jwt.security.entity.Admin;
import com.nikki.jwt.security.repository.AdminRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final SecurityUserService securityUserService;
    private final ValidationUtil validationUtil;

    public AdminResponse createAdmin(CreateAdminRequest request) {
        validationUtil.validationRequest(request);
        if (securityUserService.securityUserExistsByEmail(request.getEmail())) {
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        securityUserService.saveSecurityUser(request, ROLE.ADMIN.name());
        Admin admin = saveAdmin(request);
        return mapToAdminResponse(admin);
    }

    private Admin saveAdmin(CreateAdminRequest request) {
        Admin admin = Admin.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return adminRepository.save(admin);
    }

    public AdminResponse removeAdminByEmail(String email) {
        Admin admin;
        try {
            admin = findAdminByEmail(email);
        } catch (UsernameNotFoundException ex) {
            throw HandledException.builder()
                    .message("Admin doesn't exist")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        adminRepository.delete(admin);
        securityUserService.deleteSecurityUserByEmail(email);
        return mapToAdminResponse(admin);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Admin with email: " + email + " not found")
        );
    }

    public AdminResponse getAdminProfile() {
        return getAdminProfileByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public AdminResponse getAdminProfileByEmail(String email) {
        Admin admin = findAdminByEmail(email);
        return mapToAdminResponse(admin);
    }

    public AdminResponse changeAdminInfoSelf(ChangeAdminInfoRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
        return changeAdminInfoByEmail(SecurityContextHolder.getContext().getAuthentication().getName(), request);
    }

    private AdminResponse changeAdminInfoByEmail(String email, ChangeAdminInfoRequest request) {
        Admin admin = findAdminByEmail(email);
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPhoneNumber(request.getPhoneNumber());
        adminRepository.save(admin);
        return mapToAdminResponse(admin);
    }

    private AdminResponse mapToAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .email(admin.getEmail())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }
}
