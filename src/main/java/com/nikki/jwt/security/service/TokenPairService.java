package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.repository.RefreshTokenRepository;
import com.nikki.jwt.security.repository.TokenRepository;
import com.nikki.jwt.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TokenPairService {
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final long JWT_LIVE_TIME;
    private final long JWT_REFRESH_LIVE_TIME;

    public TokenPairService(
            TokenRepository tokenRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtil jwtUtil,
            @Value("${JWT_LIVE_TIME_MILLIS}") long JWT_LIVE_TIME,
            @Value("${JWT_REFRESH_LIVE_TIME_MILLIS}") long JWT_REFRESH_LIVE_TIME
    ) {
        this.tokenRepository = tokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.JWT_LIVE_TIME = JWT_LIVE_TIME;
        this.JWT_REFRESH_LIVE_TIME = JWT_REFRESH_LIVE_TIME;
    }

    public Optional<Token> findByJwtToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByJwtRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteAllTokensAndRefreshTokensBySecurityUserEmail(String email) {
        tokenRepository.deleteTokensBySecurityUserEmail(email);
        refreshTokenRepository.deleteRefreshTokensBySecurityUserEmail(email);
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    public TokenPair createAndSaveTokenPair(SecurityUser securityUser) {
        TokenPair tokenPair = createTokenPair(securityUser);
        saveTokenPair(securityUser, tokenPair);
        return tokenPair;
    }

    public TokenPair createTokenPair(SecurityUser securityUser) {
        return jwtUtil.generateTokenPair(securityUser);
    }

    public void saveTokenPair(SecurityUser securityUser, TokenPair tokenPair) {
        deleteAllTokensAndRefreshTokensBySecurityUserEmail(securityUser.getEmail());
        Token token = Token.builder()
                .token(tokenPair.getAccessToken())
                .securityUser(securityUser)
                .expiresAt(new Date(System.currentTimeMillis() + JWT_LIVE_TIME))
                .build();
        tokenRepository.save(token);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenPair.getRefreshToken())
                .securityUser(securityUser)
                .expiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_LIVE_TIME))
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
