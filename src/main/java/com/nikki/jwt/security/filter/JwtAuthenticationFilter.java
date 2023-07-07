package com.nikki.jwt.security.filter;

import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.service.TokenPairService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenPairService tokenPairService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        /*
            JWT Filter runs each time when request is sent from Client,
            It fires even on request which id sent to not protected route
            ( routes with .permitAll() FilterChain method )
        */
        log.info("JWT FILTER STARTED:");
        log.info("Method: {}, Request URL: {}", request.getMethod(), request.getRequestURL());
        log.info("Request URI: {}, Query String: {}", request.getRequestURI(), request.getQueryString());


        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring("Bearer ".length());
        log.trace("JWT Token in Filter: {}", jwt);

        Optional<Token> token = tokenPairService.findByJwtToken(jwt);
        if (token.isEmpty()) {
            log.trace("At the moment this check will work for both Tokens and Refresh Tokens");
            log.trace("But when Refresh Token will be sent in Cookie, this check will work only with Tokens, " +
                    " just as it is originally intended");
            log.trace("And this is when instead of checking it hear, it will be possible to throw Exceptions " +
                    " right in findByToken Method itself.");
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail;
        try {
            userEmail = tokenPairService.extractUsername(jwt);
        } catch (Exception ex) {
            log.error("From JWT Authentication Filter: {}", ex.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // КОНТЕКСТ УСТАНАВЛИВАЕТСЯ КАЖДЫЙ РАЗ С КАЖДЫМ ЗАПРОСОМ.
        // ЭТУ ПРОВЕРКУ МОЖНО НЕ ДЕЛАТЬ ЕСЛИ ИСПОЛЬЗУЕТСЯ ТОЛЬКО ОДИН ФИЛЬТР (ЭТОТ)
        // ЕСЛИ ЖЕ ФИЛЬТРОВ ХОТЯ БЫ ДВА, ТО ПРЕЖДЕ ЧЕМ ЗАПРОС ДОСТИГ ДАННОГО ФИЛЬТРА
        // ВОЗМОЖНО ПОЛЬЗОВАТЕЛЬ УЖЕ АУТЕНТИФИЦИРОВАЛСЯ В ПРЕДЫДУЩЕМ ФИЛЬТРЕ
        // И В ТАКОМ СЛУЧАЕ ОБЪЕКТ АУТЕНТИФИКАЦИИ (С ИНФОРМАЦИЕЙ О ПОЛЬЗОВАТЕЛЕ И ПРАВАМИ ДОСТУПА/РОЛЯМИ)
        // УЖЕ БЫЛ ПОЛОЖЕН В КОНТЕКСТ (SecurityContext) И НАХОДИТСЯ ТАМ.
        // В НАЧАЛЕ КАЖДОГО ЗАПРОСА КОНТЕКСТ РАВЕН = NULL
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails securityUser = userDetailsService.loadUserByUsername(userEmail);
            log.trace("Security User in JWT Filter: {}", securityUser);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    securityUser,
                    null,
                    securityUser.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("Security Context Was Set for Principal: {}.",
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        filterChain.doFilter(request, response);
    }
}
