package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.security_user.CreateSecurityUserRequest;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
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

@Transactional
@RequiredArgsConstructor
@Service
public class SecurityUserService {

    private final TokenPairService tokenPairService;
    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public SecurityUser saveSecurityUser(CreateSecurityUserRequest request, String roleName) {
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

        SecurityUser securityUser = SecurityUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
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
        if (passwordEncoder.matches(securityUser.getPassword(), issuerPassword)) {
            throw HandledException.builder()
                    .message("Issuer is not valid")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
