package com.nikki.jwt.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/client")
    public String demoClient() {
        return "Client Demo Page";
    }

    @GetMapping("/manager")
    public String demoManager() {
        return "Manager Demo Page";
    }

    @GetMapping("/admin")
    public String demoAdmin() {
        return "Admin Demo Page";
    }

    @GetMapping("/developer")
    public String demoDeveloper() {
        return "Developer Demo Page";
    }
}
