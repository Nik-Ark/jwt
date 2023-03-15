package com.nikki.jwt.security.filter;

import com.nikki.jwt.security.service.TokenPairService;
import com.nikki.jwt.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenPairService tokenPairService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring("Bearer ".length());
        final String userEmail;

        // !!!!!    CHECK IF THIS EXCEPTION IS EVER CATCHED     !!!!!
        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT_TOKEN_UNABLE_TO_GET_USERNAME " + e);
            throw e;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT_TOKEN_EXPIRED " + e);
            throw e;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails securityUser = userDetailsService.loadUserByUsername(userEmail);
            boolean tokenNotRevoked = tokenPairService.findByJwtToken(jwt)
                    .map(token -> !token.isRevoked())
                    .orElse(false);

            if (jwtUtil.isTokenValid(jwt, securityUser) && tokenNotRevoked) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
