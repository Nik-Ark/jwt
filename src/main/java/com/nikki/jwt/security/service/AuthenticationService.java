package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.dto.login.LoginRequest;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.Role;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserService securityUserService;
    private final ClientService clientService;
    private final TokenPairService tokenPairService;
    private final JwtUtil jwtUtil;

    public SecurityUserResponse register(CreateClientRequest request) {

        SecurityUser securityUser = clientService.createClient(request);

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        return mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public SecurityUserResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(request.getEmail());

        TokenPair tokenPair = jwtUtil.generateTokenPair(securityUser);
        tokenPairService.saveTokenPair(securityUser, tokenPair);

        return mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public SecurityUserResponse refreshToken(HttpServletRequest request) {

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

        return mapToSecurityUserResponse(securityUser, tokenPair);
    }

    private SecurityUserResponse mapToSecurityUserResponse(SecurityUser securityUser, TokenPair tokenPair) {
        return SecurityUserResponse.builder()
                .email(securityUser.getEmail())
                .roles(securityUser.getRoles().stream().map(Role::getName).toArray(String[] ::new))
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();
    }
}
