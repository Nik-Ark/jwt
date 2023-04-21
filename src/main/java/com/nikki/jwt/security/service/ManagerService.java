package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.manager.CreateManagerRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.repository.ManagerRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN ROLE HERE (if needed)
@RequiredArgsConstructor
@Service
public class ManagerService {

    private final SecurityUserService securityUserService;
    private final ManagerRepository managerRepository;
    private final ValidationUtil validationUtil;

    public List<ManagerResponse> getManagers(Integer count) {
        long total = managerRepository.count();
        if (total == 0) {
            return new ArrayList<>();
        }
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Manager> managerPage = managerRepository.findAll(Pageable.ofSize(finalCount));
        return managerPage.getContent().stream()
                .map(this::mapToManagerResponse).collect(Collectors.toList());
    }

    public ManagerResponse createManager(CreateManagerRequest request) {
        validationUtil.validationRequest(request);
        if (securityUserService.securityUserExistsByEmail(request.getEmail())) {
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        securityUserService.saveSecurityUser(request, ROLE.MANAGER.name());
        Manager manager = saveManager(request);
        return mapToManagerResponse(manager);
    }

    private Manager saveManager(CreateManagerRequest request) {
        Manager manager = Manager.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return managerRepository.save(manager);
    }

    public ManagerResponse removeManager(String email) {
        Manager manager;
        try {
            manager = findManagerByEmail(email);
        } catch (UsernameNotFoundException ex) {
            throw HandledException.builder()
                    .message("Manager doesn't exist")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        managerRepository.delete(manager);
        securityUserService.deleteSecurityUserByEmail(email);
        return mapToManagerResponse(manager);
    }

    public Manager findManagerByEmail(String email) {
        return managerRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Manager with email: " + email + " not found")
        );
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
