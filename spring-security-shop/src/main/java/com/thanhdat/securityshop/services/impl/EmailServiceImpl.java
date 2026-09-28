package com.thanhdat.securityshop.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.thanhdat.securityshop.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendOtp(String email, String otp, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject(subject);
        message.setText("""
                Xin chào,

                Mã OTP của bạn là: %s

                OTP có hiệu lực trong 5 phút và chỉ được sử dụng một lần.
                Không chia sẻ mã này cho người khác.
                """.formatted(otp));

        mailSender.send(message);
    }
}