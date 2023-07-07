package com.nikki.jwt.security.service;

import com.nikki.jwt.security.repository.RefreshTokenRepository;
import com.nikki.jwt.security.repository.RegisterApplicantRepository;
import com.nikki.jwt.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SchedulerService {
    private final RegisterApplicantRepository registerApplicantRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Scheduled(fixedRateString = "${REGISTER_APPLICANT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredRegisterApplicantsDeletion() {
        Date now = new Date(System.currentTimeMillis());
        log.info("Register Applicant Scheduler runs...");
        registerApplicantRepository.deleteAllExpiredSince(now);
        log.info("All expired Register Applicant entries deleted from DB");
    }


    @Scheduled(fixedRateString = "${JWT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredJWTokensDeletion() {
        log.info("JWT Scheduler runs...");
        Date now = new Date(System.currentTimeMillis());
        tokenRepository.deleteAllExpiredTokensSince(now);
        log.info("All expired JWT entries deleted from DB");
    }


    @Scheduled(fixedRateString = "${REFRESH_JWT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredRefreshJWTokensDeletion() {
        log.info("Refresh JWT Scheduler runs...");
        Date now = new Date(System.currentTimeMillis());
        refreshTokenRepository.deleteAllExpiredRefreshTokensSince(now);
        log.info("All expired Refresh JWT entries deleted from DB");
    }
}
