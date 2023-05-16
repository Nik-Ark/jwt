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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenPairService {

    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

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
                .build();
        tokenRepository.save(token);

        RefreshToken refreshToken = RefreshToken.builder()
                    .token(tokenPair.getRefreshToken())
                    .securityUser(securityUser)
                    .build();
        refreshTokenRepository.save(refreshToken);
    }
}
