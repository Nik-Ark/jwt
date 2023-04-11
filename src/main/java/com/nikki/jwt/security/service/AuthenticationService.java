package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.*;
import com.nikki.jwt.security.entity.Client;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.ClientRepository;
import com.nikki.jwt.security.repository.RoleRepository;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import com.nikki.jwt.security.util.JwtUtil;
import com.nikki.jwt.security.util.ValidationUtil;
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
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final TokenPairService tokenPairService;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;

    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto request) {

        validationUtil.validationRequest(request);

        String email = request.getEmail();
        if (securityUserRepository.existsByEmail(email)) {
            throw new BadCredentialsException("User already registered");
        }

        Optional<Role> retrievedRole = roleRepository.findByName("CLIENT");
        Set<Role> roles = new HashSet<>();
        retrievedRole.ifPresent(roles::add);

        ///////////////////////////////////////////////////////////////////////////////////////////
        /*                                                                                       */
        /*               ВЫНЕСТИ В ОТДЕЛЬНЫЕ МЕТОДЫ СОЗДАНИЕ КЛИЕНТА, МЕНЕДЖЕРА.                 */
        /*                       МЕТОДЫ ЗАЩИЩЁННЫЕ АННОТАЦИЕЙ ПО ПРАВАМ.                         */
        /*                                 1). ВАЛИДАЦИЯ.                                        */
        /*         2). ЧТО ЕСЛИ EMAIL УЖЕ СУЩЕСТВУЕТ, ВЫБРОСИТЬ ОШИБКУ С ОПИСАНИЕМ.              */
        /*                                                                                       */
        ///////////////////////////////////////////////////////////////////////////////////////////
        SecurityUser securityUser = SecurityUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .tokens(new ArrayList<>())
                .build();
        securityUserRepository.save(securityUser);

        Client client = Client.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber("+79116993890")
                .city("Moscow")
                .build();
        clientRepository.save(client);
        ///////////////////////////////////////////////////////////////////////////////////////////

        TokenPairDto tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        RegisterResponseDto response = RegisterResponseDto.builder()
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
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

        ///////////////////////////////////////////////////////////////////////////////////////////
        /*                                                                                       */
        /*       ОБРАБАТЫВАТЬ ЭТОТ EXCEPTION ЧТОБЫ СЕРВЕР ВОЗВРАЩАЛ ОШИБКУ USER_NOT_FOUND        */
        /*                                                                                       */
        ///////////////////////////////////////////////////////////////////////////////////////////
        SecurityUser securityUser = securityUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + request.getEmail() + " not found")
        );
        ///////////////////////////////////////////////////////////////////////////////////////////

        TokenPairDto tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        LoginResponseDto response = LoginResponseDto.builder()
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
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
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
