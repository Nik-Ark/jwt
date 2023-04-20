package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getClients(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint client (Get), request sent by principal: {}, with request param: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<ClientResponse> clients = clientService.getClients(count);

        log.info("END endpoint client (Get), success response containing {} clients.", clients.size());
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
