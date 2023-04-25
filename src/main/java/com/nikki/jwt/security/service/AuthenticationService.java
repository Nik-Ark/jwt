package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.login.LoginRequest;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserService securityUserService;
    private final ClientService clientService;
    private final TokenPairService tokenPairService;

    public SecurityUserResponse register(CreateClientRequest request) {
        ClientResponse clientResponse = clientService.createClient(request);
        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(clientResponse.getEmail());
        return tokenPairService.createAndSaveTokenPair(securityUser);
    }

    public SecurityUserResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication exception in Login: {}", request);
            throw authenticationException;
        }


        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(request.getEmail());
        return tokenPairService.createAndSaveTokenPair(securityUser);
    }

    public SecurityUserResponse refreshToken(HttpServletRequest request) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            log.error("No Bearer Header");
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        final String refreshJwt = authHeader.substring("Bearer ".length());
        RefreshToken refreshToken = tokenPairService.findByJwtRefreshToken(refreshJwt).orElse(null);
        if (refreshToken == null || refreshToken.isRevoked()) {
            log.error("Invalid refresh token");
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        final String userEmail = tokenPairService.extractUsername(refreshJwt);
        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(userEmail);
        return tokenPairService.createAndSaveTokenPair(securityUser);
    }
}
