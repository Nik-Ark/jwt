package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.client.ClientResponse;
import com.nikki.jwt.security.dto.client.CreateClientRequest;
import com.nikki.jwt.security.dto.confirm_email.ConfirmRegisterMailMessage;
import com.nikki.jwt.security.dto.security_user.SecurityUserResponse;
import com.nikki.jwt.security.dto.login.LoginRequest;
import com.nikki.jwt.security.dto.token.TokenPair;
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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserService securityUserService;
    private final ClientService clientService;
    private final TokenPairService tokenPairService;
    private final ValidationService validationService;
    private final EmailSenderService emailSenderService;

    public SecurityUserResponse register(CreateClientRequest request) {
        validationService.validateRequest(request);
        validationService.validateSecurityUserDoesNotExistByEmail(request.getEmail());

        // CREATING CANDIDATE FOR REGISTRATION AND SENDING LINK TO EMAIL
        emailSenderService.sendEmail(
                ConfirmRegisterMailMessage.builder()
                        .to(request.getEmail())
                        .subject("Registration confirmation on adminchakra.striving.live")
                        .message("You account successfully created!")
                        .build()
        );

        ClientResponse clientResponse = clientService.createClient(request);
        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(clientResponse.getEmail());
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
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
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
    }

    public SecurityUserResponse refreshToken(HttpServletRequest request) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            log.error("No Bearer Header");
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        final String refreshJwt = authHeader.substring("Bearer ".length());
        Optional<RefreshToken> refreshToken = tokenPairService.findByJwtRefreshToken(refreshJwt);

        if (refreshToken.isEmpty()) {
            log.error("No Refresh Token in DB");
            throw new BadCredentialsException("Correct Jwt Refresh token is not provided");
        }

        final String userEmail = tokenPairService.extractUsername(refreshJwt);
        SecurityUser securityUser = securityUserService.findSecurityUserByEmail(userEmail);
        TokenPair tokenPair = tokenPairService.createAndSaveTokenPair(securityUser);
        return securityUserService.mapToSecurityUserResponse(securityUser, tokenPair);
    }
}
