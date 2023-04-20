package com.nikki.jwt.security.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ClientResponse {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String city;
}
