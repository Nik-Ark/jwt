package com.nikki.jwt.security.service;

import com.nikki.jwt.security.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Service
@Transactional
public class SchedulerService {

    private final TokenRepository tokenRepository;

    @Scheduled(fixedRate = 120000)
    public void scheduledExpiredTokensDeletion() {
        Date now = new Date(System.currentTimeMillis());
        tokenRepository.deleteAllExpiredSince(now);
    }
}
