package com.nikki.jwt.security.dto.role;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private String name;

    @Override
    public String toString() {
        return "RoleResponse: {" +
                " name: '" + name + '\'' +
                " " + '}';
    }
}
