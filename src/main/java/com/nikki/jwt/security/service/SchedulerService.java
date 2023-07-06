package com.nikki.jwt.security.service;

import com.nikki.jwt.security.repository.RefreshTokenRepository;
import com.nikki.jwt.security.repository.RegisterApplicantRepository;
import com.nikki.jwt.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {
    private final RegisterApplicantRepository registerApplicantRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Scheduled(fixedRateString = "${REGISTER_APPLICANT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredRegisterApplicantsDeletion() {
        Date now = new Date(System.currentTimeMillis());
        registerApplicantRepository.deleteAllExpiredSince(now);
    }


    @Scheduled(fixedRateString = "${JWT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredJWTokensDeletion() {
        Date now = new Date(System.currentTimeMillis());
        tokenRepository.deleteAllExpiredTokensSince(now);
    }


    @Scheduled(fixedRateString = "${REFRESH_JWT_RUN_SCHEDULER_INTERVAL}")
    public void scheduledExpiredRefreshJWTokensDeletion() {
        Date now = new Date(System.currentTimeMillis());
        refreshTokenRepository.deleteAllExpiredRefreshTokensSince(now);
    }
}
