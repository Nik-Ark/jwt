package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.role.RoleResponse;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public int createRolesIfNotExist(List<String> roleNames) {
        int rolesCreated = 0;
        for (String roleName : roleNames) {
            rolesCreated += createRoleIfNotExists(roleName) ? 1 : 0;
        }
        log.info("Total Number of created Roles: {}.", rolesCreated);
        return rolesCreated;
    }

    public boolean createRoleIfNotExists(String roleName) {
        Role role = findByRoleName(roleName);
        boolean isCreated = role == null;
        if (isCreated) {
            role = createRole(roleName);
        }
        RoleResponse roleResponse = mapToRoleResponse(role);
        if (isCreated) {
            log.info("Role: {} has been created.", roleResponse);
            return true;
        }
        log.info("Role: {} already exists and thus won't be created.", roleResponse);
        return false;
    }

    private Role createRole(String roleName) {
        Role role = Role.builder()
                .name(roleName)
                .build();
        return roleRepository.save(role);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder()
                .name(role.getName())
                .build();
    }
}
