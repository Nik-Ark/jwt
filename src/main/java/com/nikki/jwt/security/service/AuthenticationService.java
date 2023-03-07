package com.nikki.jwt.security.service;

import com.nikki.jwt.app.entity.AppUser;
import com.nikki.jwt.app.repository.AppUserRepository;
import com.nikki.jwt.security.dto.LoginRequestDto;
import com.nikki.jwt.security.dto.RegisterRequestDto;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<String> register(RegisterRequestDto request) {

        AppUser appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        appUserRepository.save(appUser);

        // CHECK APP USER
        System.out.println(appUser);

        // MAKE ROLES STATIC FROM ENUM
        Optional<Role> retrievedRole = roleRepository.findByName("CLIENT");
        Set<Role> roles = new HashSet<>();
        retrievedRole.ifPresent(roles::add);

        SecurityUser securityUser = SecurityUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .appUser(appUser)
                .build();
        securityUserRepository.save(securityUser);

        // CHECK SECURITY USER
        System.out.println(securityUser);

        String token = jwtService.generateToken(securityUser);

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    public ResponseEntity<String> login(LoginRequestDto request) {



        String token = "";

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
