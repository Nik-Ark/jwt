package com.nikki.jwt.security.service;

import com.nikki.jwt.security.domen.api.RefreshResponse;
import com.nikki.jwt.security.domen.api.TokenPair;
import com.nikki.jwt.security.domen.api.login.LoginRequest;
import com.nikki.jwt.security.domen.api.login.LoginResponse;
import com.nikki.jwt.security.domen.api.register.RegisterRequest;
import com.nikki.jwt.security.domen.api.register.RegisterResponse;
import com.nikki.jwt.security.domen.response.exception.HandledException;
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

    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {

        validationUtil.validationRequest(request);

        if (securityUserRepository.existsByEmail(request.getEmail())) {
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
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

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        RegisterResponse response = RegisterResponse.builder()
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

//        DEFAULT USERS MUST USE SECURE PASSWORDS AND CORRECT EMAILS
//        validationUtil.validationRequest(request);

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

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        LoginResponse response = LoginResponse.builder()
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*    THIS METHOD WILL BE VOID WHEN REFRESHTOKEN RETURNS IN COOKIE      */
    public ResponseEntity<RefreshResponse> refreshToken(HttpServletRequest request) {

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

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        RefreshResponse response = RefreshResponse.builder()
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
