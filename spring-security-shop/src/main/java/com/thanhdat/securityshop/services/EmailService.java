package com.thanhdat.securityshop.services;

public interface EmailService {

    void sendOtp(String email, String otp, String subject);
}