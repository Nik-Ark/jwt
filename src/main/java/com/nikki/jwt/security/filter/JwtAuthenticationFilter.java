package com.nikki.jwt.security.filter;

import com.nikki.jwt.security.service.TokenPairService;
import com.nikki.jwt.security.util.JwtUtil;
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

        // TRY CATCH HERE !!!
        // BUT INSTEAD IT IS BETTER TO IMPROVE LOGIC HERE
        // FIRST I SHOULD CHECK IF THIS TOKEN IS PRESENT IN THE DATABASE
        // AND AFTER GET USERNAME FROM IT
        final String userEmail = jwtUtil.extractUsername(jwt);

        // КОНТЕКСТ УСТАНАВЛИВАЕТСЯ КАЖДЫЙ РАЗ С КАЖДЫМ ЗАПРОСОМ.
        // ЭТУ ПРОВЕРКУ МОЖНО НЕ ДЕЛАТЬ ЕСЛИ ИСПОЛЬЗУЕТСЯ ТОЛЬКО ОДИН ФИЛЬТР (ЭТОТ)
        // ЕСЛИ ЖЕ ФИЛЬТРОВ ХОТЯ БЫ ДВА, ТО ПРЕЖДЕ ЧЕМ ЗАПРОС ДОСТИГ ДАННОГО ФИЛЬТРА
        // ВОЗМОЖНО ПОЛЬЗОВАТЕЛЬ УЖЕ АУТЕНТИФИЦИРОВАЛСЯ В ПРЕДЫДУЩЕМ ФИЛЬТРЕ
        // И В ТАКОМ СЛУЧАЕ ОБЪЕКТ АУТЕНТИФИКАЦИИ (С ИНФОРМАЦИЕЙ О ПОЛЬЗОВАТЕЛЕ И ПРАВАМИ ДОСТУПА/РОЛЯМИ)
        // УЖЕ БЫЛ ПОЛОЖЕН В КОНТЕКСТ (SecurityContext) И НАХОДИТСЯ ТАМ.
        // В НАЧАЛЕ КАЖДОГО ЗАПРОСА КОНТЕКСТ РАВЕН = NULL
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

                System.out.println("Security Context Was Set for username " + userEmail);
            }
        }

        filterChain.doFilter(request, response);
    }
}
