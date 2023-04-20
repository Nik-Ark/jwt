package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN ROLE HERE (if needed)
@RequiredArgsConstructor
@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public List<ManagerResponse> getManagers(Integer count) {
        long total = managerRepository.count();
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Manager> managerPage = managerRepository.findAll(Pageable.ofSize(finalCount));
        List<ManagerResponse> managerResponseList = managerPage.getContent().stream()
                .map(this::mapToManagerResponse).collect(Collectors.toList());
        return managerResponseList;
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
