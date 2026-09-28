package com.thanhdat.securityshop.controllers;

import jakarta.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thanhdat.securityshop.dtos.ForgotPasswordDTO;
import com.thanhdat.securityshop.dtos.RegisterDTO;
import com.thanhdat.securityshop.dtos.ResetPasswordDTO;
import com.thanhdat.securityshop.dtos.VerifyOtpDTO;
import com.thanhdat.securityshop.services.AuthService;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDTO") RegisterDTO dto,
            BindingResult result,
            RedirectAttributes redirect
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(dto);
            redirect.addFlashAttribute("success", "OTP đã được gửi đến email");
            redirect.addAttribute("email", dto.getEmail());
            return "redirect:/verify-otp";
        } catch (IllegalArgumentException exception) {
            result.reject("register.error", exception.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam(required = false) String email,
            Model model
    ) {
        VerifyOtpDTO dto = new VerifyOtpDTO();
        dto.setEmail(email);
        model.addAttribute("verifyOtpDTO", dto);
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @Valid @ModelAttribute("verifyOtpDTO") VerifyOtpDTO dto,
            BindingResult result,
            RedirectAttributes redirect
    ) {
        if (result.hasErrors()) {
            return "auth/verify-otp";
        }

        if (!authService.verifyRegister(dto.getEmail(), dto.getOtp())) {
            result.reject("otp.error", "OTP không hợp lệ, hết hạn hoặc quá số lần thử");
            return "auth/verify-otp";
        }

        redirect.addFlashAttribute("success", "Kích hoạt tài khoản thành công");
        return "redirect:/login";
    }

    @PostMapping("/resend-register-otp")
    public String resendRegisterOtp(
            @RequestParam String email,
            RedirectAttributes redirect
    ) {
        try {
            authService.resendRegisterOtp(email);
            redirect.addFlashAttribute("success", "Đã gửi lại OTP");
        } catch (IllegalArgumentException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }

        redirect.addAttribute("email", email);
        return "redirect:/verify-otp";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid @ModelAttribute("forgotPasswordDTO") ForgotPasswordDTO dto,
            BindingResult result,
            RedirectAttributes redirect
    ) {
        if (result.hasErrors()) {
            return "auth/forgot-password";
        }

        try {
            authService.forgotPassword(dto.getEmail());
            redirect.addFlashAttribute("success", "OTP đặt lại mật khẩu đã được gửi");
            redirect.addAttribute("email", dto.getEmail());
            return "redirect:/reset-password";
        } catch (IllegalArgumentException exception) {
            result.reject("forgot.error", exception.getMessage());
            return "auth/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPassword(
            @RequestParam(required = false) String email,
            Model model
    ) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail(email);
        model.addAttribute("resetPasswordDTO", dto);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid @ModelAttribute("resetPasswordDTO") ResetPasswordDTO dto,
            BindingResult result,
            RedirectAttributes redirect
    ) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.reject("password.error", "Mật khẩu xác nhận không đúng");
        }

        if (result.hasErrors()) {
            return "auth/reset-password";
        }

        try {
            authService.resetPassword(dto.getEmail(), dto.getOtp(), dto.getPassword());
            redirect.addFlashAttribute("success", "Đổi mật khẩu thành công");
            return "redirect:/login";
        } catch (IllegalArgumentException exception) {
            result.reject("reset.error", exception.getMessage());
            return "auth/reset-password";
        }
    }
}