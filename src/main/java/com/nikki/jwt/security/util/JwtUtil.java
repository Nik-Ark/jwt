package com.nikki.jwt.security.util;

import com.nikki.jwt.security.dto.token.TokenPair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    private final String SECRET_KEY;
    private final long JWT_LIVE_TIME;
    private final long JWT_REFRESH_LIVE_TIME;

    public JwtUtil(
            @Value("${JWT_SECRET}") String SECRET_KEY,
            @Value("${JWT_LIVE_TIME_MILLIS}") long JWT_LIVE_TIME,
            @Value("${JWT_REFRESH_LIVE_TIME_MILLIS}") long JWT_REFRESH_LIVE_TIME
    )
    {
        this.SECRET_KEY = SECRET_KEY;
        this.JWT_LIVE_TIME = JWT_LIVE_TIME;
        this.JWT_REFRESH_LIVE_TIME = JWT_REFRESH_LIVE_TIME;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private String generateToken(
            String userName,
            long TOKEN_LIVE_TIME
    ) {
        return Jwts
                .builder()
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIVE_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact(); // generates and returns Token
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            String userName,
            long TOKEN_LIVE_TIME
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIVE_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact(); // generates and returns Token
    }

    public TokenPair generateTokenPair(UserDetails userDetails) {
        final String userName = userDetails.getUsername();
        return TokenPair.builder()
                .accessToken(generateToken(userName, JWT_LIVE_TIME))
                .refreshToken(generateToken(userName, JWT_REFRESH_LIVE_TIME))
                .build();
    }

    private Claims extractAllClaims(String token) {
        JwtParser jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
        Jws<Claims> jws;
        try {
            jws = jwtParser.parseClaimsJws(token);
        } catch (RuntimeException jwtException) {
            log.error("JWT Exception in jwt.util extractAllClaims: {}", jwtException.getMessage());
            throw new BadCredentialsException(jwtException.getMessage());
        }
        return jws.getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
