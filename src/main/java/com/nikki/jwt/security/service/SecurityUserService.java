package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.security_user.CreateSecurityUserRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.entity.RegisterApplicant;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SecurityUserService {

    private final TokenPairService tokenPairService;
    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;



    public SecurityUser createSecurityUser(RegisterApplicant registerApplicant, String roleName) {
        Set<Role> roles = findSecurityUserRole(roleName);
        SecurityUser securityUser = SecurityUser.builder()
                .email(registerApplicant.getEmail())
                .firstName(registerApplicant.getFirstName())
                .lastName(registerApplicant.getLastName())
                .password(registerApplicant.getPassword())
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
        return save(securityUser);
    }

    public SecurityUser createSecurityUser(CreateSecurityUserRequest request, String roleName) {
        Set<Role> roles = findSecurityUserRole(roleName);
        SecurityUser securityUser = SecurityUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
        return save(securityUser);
    }

    private Set<Role> findSecurityUserRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> {
                    log.error("Can't find ROLE: {} in DB", roleName);
                    throw HandledException.builder()
                            .message("Something went wrong")
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                }
        );
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    private SecurityUser save(SecurityUser securityUser) {
        log.info("SecurityUser saved: {}", securityUser);
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

    public SecurityUserResponse mapToSecurityUserResponse(SecurityUser securityUser, TokenPair tokenPair) {
        return SecurityUserResponse.builder()
                .email(securityUser.getEmail())
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();
    }
}
