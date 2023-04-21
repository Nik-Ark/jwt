package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.service.ClientService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping
    public ResponseEntity<ClientResponse> removeClient(@RequestParam @NotNull @NotBlank String email) {
        log.info("START endpoint removeClient, request sent by principal: {}, with request email param: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), email);

        ClientResponse response = clientService.removeClient(email);

        log.info("END endpoint removeClient, removed client: {}.", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
