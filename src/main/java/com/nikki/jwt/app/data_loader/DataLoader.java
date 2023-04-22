package com.nikki.jwt.app.data_loader;

import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.admin.AdminResponse;
import com.nikki.jwt.security.dto.admin.CreateAdminRequest;
import com.nikki.jwt.security.service.AdminService;
import com.nikki.jwt.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile(value = "dev")
public class DataLoader implements CommandLineRunner {

    private final RoleService roleService;
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {

        log.info("***********************************************************");
        log.info("START DataLoader running in Dev Profile! 🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐");

        roleService.createRolesIfNotExist(List.of(ROLE.CLIENT.name(), ROLE.MANAGER.name(), ROLE.ADMIN.name()));

        CreateAdminRequest adminRequest = CreateAdminRequest.builder()
                .email("nikki@gmail.com")
                .firstName("Nikki")
                .lastName("Alex")
                .password("Nikki1!+")
                .phoneNumber("+998974559812")
                .build();

        /*

        if (adminService.existsByEmail(adminRequest.getEmail())) {
            log.info("Admin was deleted: {}.", adminService.removeAdminByEmail("nikki@gmail.com"));
        }

        */

        if (!adminService.existsByEmail(adminRequest.getEmail())) {
            AdminResponse createdAdmin = adminService.createAdmin(adminRequest);
            log.info("Admin was created: {}.", createdAdmin);
        } else {
            Optional<AdminResponse> fetchedAdmin = adminService.exposeAdmin(adminRequest.getEmail());
            if (fetchedAdmin.isPresent()) {
                log.info("Admin already exists and won't be created: {}.", fetchedAdmin.get());
            } else {
                log.error("Something went wrong, Admin with email: {} doesn't exist and wasn't created.",
                        adminRequest.getEmail());
            }
        }

        log.info("END DataLoader running in Dev Profile! 🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐");
        log.info("***********************************************************");
    }
}
