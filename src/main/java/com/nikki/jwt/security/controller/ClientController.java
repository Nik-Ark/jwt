package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/get")
    public List<Client> getClients(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint client/get, request sent by principal: {}, with request param: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<Client> clients = clientService.getClients(count);

        log.info("END endpoint client/get, success response containing {} clients.", clients.size());
        return clients;
    }
}
