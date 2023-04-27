package com.nikki.jwt.security.service;

import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.client.ChangeClientInfoRequest;
import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.entity.*;
import com.nikki.jwt.security.repository.*;
import com.nikki.jwt.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// METHOD AUTHENTICATION BY ADMIN AND MANAGER ROLES HERE (if needed)
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final SecurityUserService securityUserService;
    private final ClientRepository clientRepository;
    private final ValidationUtil validationUtil;

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

    public ClientResponse createClient(CreateClientRequest request) {
        validationUtil.validationRequest(request);
        if (securityUserService.securityUserExistsByEmail(request.getEmail())) {
            log.error("nickname already exists: {}", request.getEmail());
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }
        SecurityUser securityUser = securityUserService.createSecurityUser(request, ROLE.CLIENT.name());
        Client client = saveClient(request, securityUser);
        return mapToClientResponse(client);
    }

    private Client saveClient(CreateClientRequest request, SecurityUser securityUser) {
        Client client = Client.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .securityUser(securityUser)
                .build();
        return save(client);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public ClientResponse removeClientSelf() {
        return removeClientByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ClientResponse removeClientSuperior(String targetUserEmail) {
        ClientResponse clientResponse;
        try {
            clientResponse = removeClientByEmail(targetUserEmail);
        } catch (UsernameNotFoundException exception) {
            log.error("Client with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return clientResponse;
    }

    private ClientResponse removeClientByEmail(String email) {
        Client client;
        try {
            client = findClientByEmail(email);
        } catch (UsernameNotFoundException ex) {
            throw HandledException.builder()
                    .message("Client doesn't exist")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        clientRepository.delete(client);
        securityUserService.deleteSecurityUserByEmail(email);
        return mapToClientResponse(client);
    }

    public boolean clientExistsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Client with email: " + email + " not found")
        );
    }

    public ClientResponse getClientInfoSelf() {
        return getClientProfileByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ClientResponse getClientInfoSuperior(String targetUserEmail) {
        ClientResponse clientResponse;
        try {
            clientResponse = getClientProfileByEmail(targetUserEmail);
        } catch (UsernameNotFoundException exception) {
            log.error("Client with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return clientResponse;
    }

    private ClientResponse getClientProfileByEmail(String email) {
        Client client = findClientByEmail(email);
        return mapToClientResponse(client);
    }

    public ClientResponse changeClientInfoSelf(ChangeClientInfoRequest request) {
        validateRequestAndIssuerPassword(request);
        return changeClientInfoByEmail(request, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ClientResponse changeClientInfoSuperior(ChangeClientInfoRequest request, String targetUserEmail) {
        validateRequestAndIssuerPassword(request);
        ClientResponse clientResponse;
        try {
            clientResponse = changeClientInfoByEmail(request, targetUserEmail);
        } catch (UsernameNotFoundException exception) {
            log.error("Client with email: {} Not Found", targetUserEmail);
            throw HandledException.builder()
                    .message("Bad request")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return clientResponse;
    }

    private ClientResponse changeClientInfoByEmail(ChangeClientInfoRequest request, String targetUserEmail) {
        Client client = findClientByEmail(targetUserEmail);
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber() == null ? client.getPhoneNumber() : request.getPhoneNumber());
        client.setCity(request.getCity() == null ? client.getCity() : request.getCity());
        clientRepository.save(client);
        return mapToClientResponse(client);
    }

    private void validateRequestAndIssuerPassword(ChangeClientInfoRequest request) {
        validationUtil.validationRequest(request);
        securityUserService.validateIssuerPassword(request.getIssuerPassword());
    }

    public ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.builder()
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .city(client.getCity())
                .build();
    }
}
