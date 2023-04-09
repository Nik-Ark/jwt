package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    private final ManagerService managerService;

    // REPLACE ARGUMENT ON INT
    @GetMapping("/get-clients")
    public List<Client> getClients(@RequestParam(defaultValue = "20") Integer count) {
        return managerService.getClients(count);
    }
}
