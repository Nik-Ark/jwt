package com.nikki.jwt.security.service;

import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Correct Jwt token is not provided");
        }

        final String jwt = authHeader.substring("Bearer ".length());
        Token token = tokenRepository.findByToken(jwt).orElse(null);
        if (token == null || token.isRevoked()) {
            throw new RuntimeException("Correct Jwt token is not provided");
        }

        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
