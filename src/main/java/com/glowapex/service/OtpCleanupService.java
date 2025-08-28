package com.glowapex.service;

import com.glowapex.entity.OtpEntity;
import com.glowapex.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OtpCleanupService {

    @Autowired
    private OtpRepository otpRepository;

    // Run every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void removeExpiredOtps() {
        List<OtpEntity> expiredOtps = otpRepository.findAll().stream()
                .filter(otp -> otp.getExpiry().isBefore(LocalDateTime.now()))
                .toList();

        if (!expiredOtps.isEmpty()) {
            otpRepository.deleteAll(expiredOtps);
            System.out.println("Cleaned up " + expiredOtps.size() + " expired OTPs");
        }
    }
}