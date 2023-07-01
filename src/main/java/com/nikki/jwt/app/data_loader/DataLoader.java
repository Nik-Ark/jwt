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

@Slf4j
@Component
@RequiredArgsConstructor
@Profile(value = "production")
public class DataLoader implements CommandLineRunner {

    private final RoleService roleService;
    private final AdminService adminService;

    @Override
    public void run(String... args) {

        log.info("***********************************************************");
        log.info("START DataLoader 🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐");

        roleService.createRolesIfNotExist(List.of(ROLE.CLIENT.name(), ROLE.MANAGER.name(), ROLE.ADMIN.name()));

        CreateAdminRequest adminRequest_1 = CreateAdminRequest.builder()
                .email("admin@prime.plus")
                .firstName("Nikita")
                .lastName("Alekseev")
                .password("Nikitos111&!")
                .phoneNumber("+79116993890")
                .build();

        if (!adminService.adminExistsByEmail(adminRequest_1.getEmail())) {
            AdminResponse createdAdmin = adminService.createAdmin(adminRequest_1);
            log.info("Admin was created: {}.", createdAdmin);
        } else {
            AdminResponse fetchedAdmin = adminService.getAdminInfoSuperior(adminRequest_1.getEmail());
            log.info("Admin already exists and won't be created: {}.", fetchedAdmin);
        }

        CreateAdminRequest adminRequest_2 = CreateAdminRequest.builder()
                .email("employer@demo.admin")
                .firstName("Employer")
                .lastName("Hire_Me")
                .password("PotentialEmployer777$")
                .phoneNumber("+79999999999")
                .build();

        if (!adminService.adminExistsByEmail(adminRequest_2.getEmail())) {
            AdminResponse createdAdmin = adminService.createAdmin(adminRequest_2);
            log.info("Admin was created: {}.", createdAdmin);
        } else {
            AdminResponse fetchedAdmin = adminService.getAdminInfoSuperior(adminRequest_2.getEmail());
            log.info("Admin already exists and won't be created: {}.", fetchedAdmin);
        }

        log.info("END DataLoader 🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐🖐");
        log.info("***********************************************************");
    }
}
