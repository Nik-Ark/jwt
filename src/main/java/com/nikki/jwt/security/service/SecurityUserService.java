package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.security_user.CreateSecurityUserRequest;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityUserService {

    private final TokenPairService tokenPairService;
    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ValidationUtil validationUtil;

    public SecurityUser createSecurityUser(CreateSecurityUserRequest request, String roleName) {
        validationUtil.validationRequest(request);
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> {
                    throw HandledException.builder()
                            .message("Can't find ROLE " + roleName + " in Roles DB")
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                }
        );
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return saveSecurityUser(request, roles);
    }

    private SecurityUser saveSecurityUser(CreateSecurityUserRequest request, Set<Role> roles) {
        SecurityUser securityUser = SecurityUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
        return save(securityUser);
    }

    public SecurityUser save(SecurityUser securityUser) {
        return securityUserRepository.save(securityUser);
    }

    public boolean securityUserExistsByEmail(String email) {
        return securityUserRepository.existsByEmail(email);
    }

    public void deleteSecurityUserByEmail(String email) {
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(email);
        securityUserRepository.deleteSecurityUserByEmail(email);
    }

    public SecurityUser findSecurityUserByEmail(String email) {
        return securityUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + email + " not found")
        );
    }

    public void validateIssuerPassword(String issuerPassword) {
        SecurityUser securityUser =
                findSecurityUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!passwordEncoder.matches(issuerPassword, securityUser.getPassword())) {
            throw HandledException.builder()
                    .message("Issuer is not valid")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
