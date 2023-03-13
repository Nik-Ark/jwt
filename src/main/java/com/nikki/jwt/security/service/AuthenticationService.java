package com.nikki.jwt.security.service;

import com.nikki.jwt.app.entity.AppUser;
import com.nikki.jwt.security.dto.LoginRequestDto;
import com.nikki.jwt.security.dto.RegisterRequestDto;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<String> register(RegisterRequestDto request) {

        AppUser appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        Optional<Role> retrievedRole = roleRepository.findByName("CLIENT");
        Set<Role> roles = new HashSet<>();
        retrievedRole.ifPresent(roles::add);

        SecurityUser securityUser = SecurityUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .appUser(appUser)
                .tokens(new ArrayList<>())
                .build();
        securityUserRepository.save(securityUser);
        String jwtToken = jwtService.generateToken(securityUser);
        saveToken(securityUser, jwtToken);

        // CHECK SECURITY USER AND TOKEN
        System.out.println(appUser);
        System.out.println(securityUser);

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
    }

    public ResponseEntity<String> login(LoginRequestDto request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        SecurityUser securityUser = securityUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + request.getEmail() + " not found")
        );

        String jwtToken = jwtService.generateToken(securityUser);

        saveToken(securityUser, jwtToken);

        // CHECK SECURITY USER AND TOKEN
        System.out.println(securityUser.getAppUser());
        System.out.println(securityUser);

        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    private void saveToken(SecurityUser securityUser, String jwtToken) {
        revokeUserTokens(securityUser.getId());
        Token token = Token.builder()
                .token(jwtToken)
                .securityUser(securityUser)
                .build();
        tokenRepository.save(token);
    }

    private void revokeUserTokens(int id) {
        List<Token> validTokens = tokenRepository.findAllValidTokensBySecurityUserId(id);
        if (validTokens.isEmpty())
            return;

        validTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validTokens);
    }
}
