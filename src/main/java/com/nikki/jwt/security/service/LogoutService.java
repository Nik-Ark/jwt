package com.nikki.jwt.security.service;

import com.nikki.jwt.security.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {
    private final TokenPairService tokenPairService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    )
    {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Correct Jwt token is not provided");
        }

        final String jwt = authHeader.substring("Bearer ".length());
        Token token = tokenPairService.findByJwtToken(jwt).orElse(null);
        if (token == null || token.isRevoked()) {
            throw new BadCredentialsException("Correct Jwt token is not provided");
        }

        tokenPairService.revokeAllUserTokens(token.getSecurityUser().getId());
    }
}
