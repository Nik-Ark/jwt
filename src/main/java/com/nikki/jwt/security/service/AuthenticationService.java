package com.nikki.jwt.security.service;

import com.nikki.jwt.security.api.role.ROLE;
import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.dto.login.LoginRequest;
import com.nikki.jwt.app.response.exception.HandledException;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.JwtUtil;
import com.nikki.jwt.security.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserService securityUserService;
    private final ClientService clientService;
    private final TokenPairService tokenPairService;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;

    public ResponseEntity<SecurityUserResponse> register(CreateClientRequest request) {

        validationUtil.validationRequest(request);

        if (securityUserService.securityUserExistsByEmail(request.getEmail())) {
            throw HandledException.builder()
                    .message("This nickname already exists, please enter another nickname")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }

        SecurityUser securityUser = securityUserService.saveSecurityUser(request, ROLE.CLIENT.name());
        clientService.saveClient(request);

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        SecurityUserResponse response = mapToSecurityUserResponse(securityUser, tokenPair);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<SecurityUserResponse> login(LoginRequest request) {
        /*
        DEFAULT USERS MUST USE SECURE PASSWORDS AND CORRECT EMAILS
        validationUtil.validationRequest(request);
        */
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(request.getEmail());

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        SecurityUserResponse response = mapToSecurityUserResponse(securityUser, tokenPair);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SecurityUserResponse> refreshToken(HttpServletRequest request) {

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

        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(userEmail);

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        SecurityUserResponse response = mapToSecurityUserResponse(securityUser, tokenPair);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private SecurityUserResponse mapToSecurityUserResponse(SecurityUser securityUser, TokenPair tokenPair) {
        return SecurityUserResponse.builder()
                .firstName(securityUser.getFirstName())
                .lastName(securityUser.getLastName())
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();
    }
}
