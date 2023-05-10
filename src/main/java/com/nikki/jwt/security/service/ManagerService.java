package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.delete.DeleteRequest;
import com.nikki.jwt.security.dto.manager.ChangeManagerInfoRequest;
import com.nikki.jwt.security.dto.manager.CreateManagerRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.ManagerRepository;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN ROLE HERE (if needed)
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
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
        finalCount = Math.min(finalCount, 50);
        Page<Manager> managerPage = managerRepository.findAll(Pageable.ofSize(finalCount));
        return managerPage.getContent().stream()
                .map(this::mapToManagerResponse).collect(Collectors.toList());
    }

    public ManagerResponse createManager(CreateManagerRequest request) {
        validateRequestAndIssuerPassword(request, request.getIssuerPassword());
        if (securityUserService.securityUserExistsByEmail(request.getEmail())) {
            log.error("nickname already exists: {}", request.getEmail());
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        SecurityUser securityUser = securityUserService.createSecurityUser(request, ROLE.MANAGER.name());
        Manager manager = saveManager(request, securityUser);
        return mapToManagerResponse(manager);
    }

    private Manager saveManager(CreateManagerRequest request, SecurityUser securityUser) {
        Manager manager = Manager.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .securityUser(securityUser)
                .build();
        return save(manager);
    }

    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    public void removeManagerSelf(DeleteRequest request) {
        validateRequestAndIssuerPassword(request, request.getIssuerPassword());
        removeManagerByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void removeManagerSuperior(DeleteRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request, request.getIssuerPassword());
        removeManagerByEmail(targetUserEmail);
    }

    private void removeManagerByEmail(String email) {
        managerRepository.deleteManagerByEmail(email);
        securityUserService.deleteSecurityUserByEmail(email);
    }

    public boolean managerExistsByEmail(String email) {
        return managerRepository.existsByEmail(email);
    }

    public Manager findManagerByEmail(String email) {
        return managerRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Manager with email: " + email + " not found")
        );
    }

    public ManagerResponse getManagerInfoSelf() {
        return getManagerProfileByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ManagerResponse getManagerInfoSuperior(String targetUserEmail) {
        ManagerResponse managerResponse;
        try {
            managerResponse = getManagerProfileByEmail(targetUserEmail);
        } catch (UsernameNotFoundException exception) {
            log.error("Manager with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return managerResponse;
    }

    private ManagerResponse getManagerProfileByEmail(String email) {
        Manager manager = findManagerByEmail(email);
        return mapToManagerResponse(manager);
    }

    public ManagerResponse changeManagerInfoSelf(ChangeManagerInfoRequest request) {
        validateRequestAndIssuerPassword(request, request.getIssuerPassword());
        return changeManagerInfoByEmail(request, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ManagerResponse changeManagerInfoSuperior(ChangeManagerInfoRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request, request.getIssuerPassword());
        ManagerResponse managerResponse;
        try {
            managerResponse = changeManagerInfoByEmail(request, targetUserEmail);
        } catch (UsernameNotFoundException exception) {
            log.error("Manager with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return managerResponse;
    }

    private ManagerResponse changeManagerInfoByEmail(ChangeManagerInfoRequest request, String targetUserEmail) {
        Manager manager = findManagerByEmail(targetUserEmail);
        manager.setFirstName(request.getFirstName());
        manager.setLastName(request.getLastName());
        manager.setPhoneNumber(request.getPhoneNumber() == null ? manager.getPhoneNumber() : request.getPhoneNumber());
        managerRepository.save(manager);
        return mapToManagerResponse(manager);
    }

    private <T> void validateRequestAndIssuerPassword(T request, String password) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(password);
    }

    public ManagerResponse mapToManagerResponse(Manager manager) {
        return ManagerResponse.builder()
                .email(manager.getEmail())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .phoneNumber(manager.getPhoneNumber())
                .build();
    }
}
