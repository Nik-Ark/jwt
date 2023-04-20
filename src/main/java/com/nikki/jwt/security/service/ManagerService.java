package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.dto.manager.CreateManagerRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.ManagerRepository;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN ROLE HERE (if needed)
@RequiredArgsConstructor
@Service
public class ManagerService {

    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final ManagerRepository managerRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    public List<ManagerResponse> getManagers(Integer count) {
        long total = managerRepository.count();
        if (total == 0) {
            return new ArrayList<>();
        }
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Manager> managerPage = managerRepository.findAll(Pageable.ofSize(finalCount));
        List<ManagerResponse> managerResponseList = managerPage.getContent().stream()
                .map(this::mapToManagerResponse).collect(Collectors.toList());
        return managerResponseList;
    }

    public ManagerResponse createManager(CreateManagerRequest request) {

        validationUtil.validationRequest(request);

        if (securityUserRepository.existsByEmail(request.getEmail())) {
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }

        Role retrievedRole = roleRepository.findByName("MANAGER").orElseThrow(
                () -> {
                    throw HandledException.builder()
                            .message("Can't find ROLE MANAGER in Roles DB")
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                }
        );
        Set<Role> roles = new HashSet<>();
        roles.add(retrievedRole);

        SecurityUser securityUser = SecurityUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
        securityUserRepository.save(securityUser);

        Manager manager = Manager.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        managerRepository.save(manager);

        return ManagerResponse.builder()
                .email(manager.getEmail())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phoneNumber(manager.getPhoneNumber())
                .build();
    }

    private ManagerResponse mapToManagerResponse(Manager manager) {
        return ManagerResponse.builder()
                .email(manager.getEmail())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phoneNumber(manager.getPhoneNumber())
                .build();
    }
}
