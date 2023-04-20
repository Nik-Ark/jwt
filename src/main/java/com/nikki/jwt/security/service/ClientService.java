package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN AND MANAGER ROLES HERE (if needed)
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public List<ClientResponse> getClients(Integer count) {
        long total = clientRepository.count();
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Client> clientPage = clientRepository.findAll(Pageable.ofSize(finalCount));
        List<ClientResponse> clientResponseList = clientPage.getContent().stream()
                .map(this::mapToClientResponse).collect(Collectors.toList());
        return clientResponseList;
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
