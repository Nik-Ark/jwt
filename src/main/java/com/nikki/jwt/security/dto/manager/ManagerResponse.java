package com.nikki.jwt.security.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ManagerResponse {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
