package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/get")
    public List<Client> getClients(@RequestParam(defaultValue = "20") Integer count) {
        return clientService.getClients(count);
    }
}
