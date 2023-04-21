package com.nikki.jwt.security.service;

import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {
    private final TokenPairService tokenPairService;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    )
    {
        log.info("START endpoint logout, request Authorization Header: {}", request.getHeader("Authorization"));

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Correct Jwt token is not provided");
        }

        final String jwt = authHeader.substring("Bearer ".length());

        try {
            jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            log.error("Bad credentials in logout");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try {
                response.getWriter().write("Forbidden");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }

        Token token = tokenPairService.findByJwtToken(jwt).orElse(null);
        if (token == null || token.isRevoked()) {
            log.error("Bad credentials in logout");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try {
                response.getWriter().write("Forbidden");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            response.getWriter().write("Logged out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tokenPairService.revokeAllUserTokens(token.getSecurityUser().getId());
        log.info("END endpoint logout, {}", token.getSecurityUser());
    }
}
