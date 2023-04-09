package com.nikki.jwt.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/client")
    public String demoClient() {

        // WILL RETURN: UsernamePasswordAuthenticationToken (WITH ALL THE DETAILS INSIDE IT)
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        // WILL RETURN USERNAME (USER-EMAIL IN MY APPLICATION)
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

        return "Client Demo Page";
    }

    @GetMapping("/admin")
    public String demoAdmin() {

        // WILL RETURN: WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null]
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
        return "Admin Demo Page";
    }

    @GetMapping("/developer")
    public String demoDeveloper() {

        // WILL RETURN SECURITY USER: SecurityUser {id: 4, email: 'user4', roles: [Role: {name: 'DEVELOPER'}]}
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "Developer Demo Page";
    }
}
