package com.nikki.jwt.security.service;

import com.nikki.jwt.app.entity.AppUser;
import com.nikki.jwt.security.dto.*;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final TokenPairService tokenPairService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto request) {

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

        TokenPairDto tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        RegisterResponseDto response = RegisterResponseDto.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        SecurityUser securityUser = securityUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + request.getEmail() + " not found")
        );

        TokenPairDto tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        LoginResponseDto response = LoginResponseDto.builder()
                .firstName(securityUser.getAppUser().getFirstName())
                .lastName(securityUser.getAppUser().getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<RefreshResponseDto> refreshToken(HttpServletRequest request) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        final String refreshJwt = authHeader.substring("Bearer ".length());
        RefreshToken refreshToken = tokenPairService.findByJwtRefreshToken(refreshJwt).orElse(null);
        if (refreshToken == null || refreshToken.isRevoked()) {
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }


        final String userEmail;
        try {
            userEmail = jwtUtil.extractUsername(refreshJwt);
        } catch (Exception e) {
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        SecurityUser securityUser = securityUserRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + userEmail + " not found"));

        TokenPairDto tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        RefreshResponseDto response = RefreshResponseDto.builder()
                .firstName(securityUser.getAppUser().getFirstName())
                .lastName(securityUser.getAppUser().getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
