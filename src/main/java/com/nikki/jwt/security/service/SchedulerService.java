package com.nikki.jwt.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SchedulerService {

    private final TokenPairService tokenPairService;

    // PLANNED DELETION OF EXPIRED TOKENS FROM DATABASE
    @Scheduled(fixedRate = 1200000)
    public void scheduledExpiredTokensDeletion() {
        tokenPairService.deleteAllExpiredTokens();
    }

    // PLANNED DELETION OF EXPIRED REFRESH TOKENS FROM DATABASE
    @Scheduled(fixedRate = 2400000)
    public void scheduledExpiredRefreshTokensDeletion() {
        tokenPairService.deleteAllExpiredRefreshTokens();
    }
}
