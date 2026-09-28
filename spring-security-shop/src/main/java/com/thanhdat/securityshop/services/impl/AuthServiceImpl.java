package com.thanhdat.securityshop.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhdat.securityshop.dtos.RegisterDTO;
import com.thanhdat.securityshop.entities.Role;
import com.thanhdat.securityshop.entities.User;
import com.thanhdat.securityshop.repositories.RoleRepository;
import com.thanhdat.securityshop.repositories.UserRepository;
import com.thanhdat.securityshop.services.AuthService;
import com.thanhdat.securityshop.services.OtpService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không đúng");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER chưa tồn tại"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(dto.getFullName().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(false);
        user.setRole(role);

        userRepository.save(user);
        otpService.sendRegisterOtp(email);
    }

    @Override
    @Transactional
    public boolean verifyRegister(String email, String otp) {
        if (!otpService.verifyRegisterOtp(email, otp)) {
            return false;
        }

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        user.setEnabled(true);
        return true;
    }

    @Override
    @Transactional
    public void resendRegisterOtp(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại"));

        if (user.isEnabled()) {
            throw new IllegalArgumentException("Tài khoản đã được kích hoạt");
        }

        otpService.sendRegisterOtp(user.getEmail());
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Tài khoản chưa được kích hoạt");
        }

        otpService.sendResetPasswordOtp(user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(String email, String otp, String password) {
        if (!otpService.verifyResetPasswordOtp(email, otp)) {
            throw new IllegalArgumentException("OTP không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại"));

        user.setPassword(passwordEncoder.encode(password));
    }
}