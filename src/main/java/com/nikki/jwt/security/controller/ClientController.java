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
        log.info("START endpoint '/client' (Get), principal: {}, request param (count): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<ClientResponse> clients = clientService.getClients(count);

        log.info("END endpoint '/client' (Get), response containing {} clients.", clients.size());
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ClientResponse> deleteClientByEmail(@RequestParam @NotNull @NotBlank String email) {
        log.info("START endpoint '/client' (Delete), principal: {}, request param (email): {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), email);

        ClientResponse clientResponse = clientService.removeClientSuperior(email);

        log.info("END endpoint '/client' (Delete), deleted: {}.", clientResponse);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }
}
