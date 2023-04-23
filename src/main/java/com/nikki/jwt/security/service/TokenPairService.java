package com.nikki.jwt.security.service;

import com.nikki.jwt.security.dto.token.TokenPair;
import com.nikki.jwt.security.entity.RefreshToken;
import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.entity.Token;
import com.nikki.jwt.security.repository.RefreshTokenRepository;
import com.nikki.jwt.security.repository.TokenRepository;
import com.nikki.jwt.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenPairService {

    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public void deleteAllExpiredTokens() {
        Date now = new Date(System.currentTimeMillis());
        tokenRepository.deleteAllExpiredSince(now);
    }

    public Optional<Token> findByJwtToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteAllExpiredRefreshTokens() {
        Date now = new Date(System.currentTimeMillis());
        refreshTokenRepository.deleteAllExpiredSince(now);
    }

    public Optional<RefreshToken> findByJwtRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteAllTokensAndRefreshTokensBySecurityUserEmail(String email) {
        tokenRepository.deleteTokensBySecurityUserEmail(email);
        refreshTokenRepository.deleteRefreshTokensBySecurityUserEmail(email);
    }

    public void revokeAllUserTokens(Long id) {
        List<Token> validTokens = tokenRepository.findAllValidTokensBySecurityUserId(id);
        List<RefreshToken> validRefreshTokens = refreshTokenRepository.findAllValidRefreshTokensBySecurityUserId(id);

        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> token.setRevoked(true));
            tokenRepository.saveAll(validTokens);
        }

        if (!validRefreshTokens.isEmpty()) {
            validRefreshTokens.forEach(refreshToken -> refreshToken.setRevoked(true));
            refreshTokenRepository.saveAll(validRefreshTokens);
        }
    }

    public void saveTokenPair(SecurityUser securityUser, TokenPair tokenPair) {
        revokeAllUserTokens(securityUser.getId());
        Token token;
        try {
            token = Token.builder()
                    .token(tokenPair.getAccessToken())
                    .securityUser(securityUser)
                    .expiryDate(jwtUtil.extractExpiration(tokenPair.getAccessToken()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Can't save token in DB" + e.getMessage());
        }
        tokenRepository.save(token);

        RefreshToken refreshToken;
        try {
            refreshToken = RefreshToken.builder()
                    .token(tokenPair.getRefreshToken())
                    .securityUser(securityUser)
                    .expiryDate(jwtUtil.extractExpiration(tokenPair.getRefreshToken()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Can't save token in DB" + e.getMessage());
        }
        refreshTokenRepository.save(refreshToken);
    }
}
