package com.thanhdat.securityshop.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securityshop.entities.OtpToken;
import com.thanhdat.securityshop.repositories.OtpTokenRepository;
import com.thanhdat.securityshop.services.EmailService;
import com.thanhdat.securityshop.services.OtpService;

@Service
public class OtpServiceImpl implements OtpService {

    private static final String REGISTER = "REGISTER";
    private static final String RESET_PASSWORD = "RESET_PASSWORD";

    private final OtpTokenRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();
    private final int expirationMinutes;
    private final int maxAttempts;

    public OtpServiceImpl(
            OtpTokenRepository repository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            @Value("${app.otp.expiration-minutes:5}") int expirationMinutes,
            @Value("${app.otp.max-attempts:5}") int maxAttempts
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.expirationMinutes = expirationMinutes;
        this.maxAttempts = maxAttempts;
    }

    @Override
    @Transactional
    public void sendRegisterOtp(String email) {
        send(email, REGISTER, "Security Shop - Xác nhận đăng ký");
    }

    @Override
    @Transactional
    public boolean verifyRegisterOtp(String email, String otp) {
        return verify(email, otp, REGISTER);
    }

    @Override
    @Transactional
    public void sendResetPasswordOtp(String email) {
        send(email, RESET_PASSWORD, "Security Shop - Đặt lại mật khẩu");
    }

    @Override
    @Transactional
    public boolean verifyResetPasswordOtp(String email, String otp) {
        return verify(email, otp, RESET_PASSWORD);
    }

    private void send(String email, String type, String subject) {
        repository.deleteByEmailIgnoreCaseAndType(email, type);

        String otp = "%06d".formatted(random.nextInt(1_000_000));

        OtpToken token = new OtpToken();
        token.setEmail(email.trim().toLowerCase());
        token.setOtpHash(passwordEncoder.encode(otp));
        token.setType(type);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));
        token.setAttempts(0);
        token.setUsed(false);

        repository.save(token);
        emailService.sendOtp(email, otp, subject);
    }

    private boolean verify(String email, String otp, String type) {
        OtpToken token = repository
                .findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByCreatedAtDesc(
                        email,
                        type
                )
                .orElse(null);

        if (token == null
                || token.isUsed()
                || token.getExpiresAt().isBefore(LocalDateTime.now())
                || token.getAttempts() >= maxAttempts) {
            return false;
        }

        token.setAttempts(token.getAttempts() + 1);

        if (!passwordEncoder.matches(otp, token.getOtpHash())) {
            repository.save(token);
            return false;
        }

        token.setUsed(true);
        repository.save(token);
        return true;
    }
}