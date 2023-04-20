package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.ClientResponse;
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

    private final SecurityUserRepository securityUserRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ClientRepository clientRepository;

    public List<ClientResponse> getClients(Integer count) {
        long total = clientRepository.count();
        if (total == 0) {
            return new ArrayList<>();
        }
        int finalCount = count > total ? (int) total : count;
        finalCount = Math.min(finalCount, 20);
        Page<Client> clientPage = clientRepository.findAll(Pageable.ofSize(finalCount));
        List<ClientResponse> clientResponseList = clientPage.getContent().stream()
                .map(this::mapToClientResponse).collect(Collectors.toList());
        return clientResponseList;
    }

    public ClientResponse removeClient(String email) {

        Client client = clientRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Client with email: " + email + " not found")
        );

        SecurityUser securityUser = securityUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + email + " not found")
        );

        clientRepository.delete(client);

        List<Token> tokens = tokenRepository.findAllBySecurityUserId(securityUser.getId());
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllBySecurityUserId(securityUser.getId());
        tokenRepository.deleteAll(tokens);
        refreshTokenRepository.deleteAll(refreshTokens);

        securityUserRepository.delete(securityUser);

        return ClientResponse.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .city(client.getCity())
                .build();
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
