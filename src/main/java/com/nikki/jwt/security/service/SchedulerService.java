package com.nikki.jwt.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final TokenPairService tokenPairService;

    // PLANNED DELETION OF EXPIRED TOKENS FROM DATABASE (2 MINUTES)
    @Scheduled(fixedRate = 120000)
    public void scheduledExpiredTokensDeletion() {
        tokenPairService.deleteAllExpiredTokens();
    }

    // PLANNED DELETION OF EXPIRED REFRESH TOKENS FROM DATABASE (4 MINUTES)
    @Scheduled(fixedRate = 240000)
    public void scheduledExpiredRefreshTokensDeletion() {
        tokenPairService.deleteAllExpiredRefreshTokens();
    }
}
