package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.entity.*;
import com.nikki.jwt.security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN AND MANAGER ROLES HERE (if needed)
@RequiredArgsConstructor
@Service
public class ClientService {

    private final SecurityUserService securityUserService;
    private final ClientRepository clientRepository;

    public List<ClientResponse> getClients(Integer count) {
        long total = clientRepository.count();
        if (total == 0) {
            return new ArrayList<>();
        }
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Client> clientPage = clientRepository.findAll(Pageable.ofSize(finalCount));
        return clientPage.getContent().stream()
                .map(this::mapToClientResponse).collect(Collectors.toList());
    }

    public Client createClient(CreateClientRequest request) {
        Client client = Client.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .build();
        return clientRepository.save(client);
    }

    public ClientResponse removeClient(String email) {
        Client client = findClientByEmail(email);

        clientRepository.delete(client);
        securityUserService.deleteSecurityUserByEmail(email);

        return ClientResponse.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .city(client.getCity())
                .build();
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Client with email: " + email + " not found")
        );
    }

    private ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.builder()
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .city(client.getCity())
                .build();
    }
}
