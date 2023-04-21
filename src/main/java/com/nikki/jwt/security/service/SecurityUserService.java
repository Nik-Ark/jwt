package com.nikki.jwt.security.service;

import com.nikki.jwt.security.entity.SecurityUser;
import com.nikki.jwt.security.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SecurityUserService {

    private final TokenPairService tokenPairService;
    private final SecurityUserRepository securityUserRepository;

    public void deleteSecurityUserByEmail(String email) {
        tokenPairService.deleteAllTokensAndRefreshTokensBySecurityUserEmail(email);
        securityUserRepository.deleteSecurityUserByEmail(email);
    }

    public SecurityUser findSecurityUserByEmail(String email) {
        return securityUserRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + email + " not found")
        );
    }
}
