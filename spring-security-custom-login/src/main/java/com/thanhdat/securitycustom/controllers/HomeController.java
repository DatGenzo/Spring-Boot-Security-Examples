package com.thanhdat.securitycustom.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thanhdat.securitycustom.entities.User;
import com.thanhdat.securitycustom.mappers.UserMapper;
import com.thanhdat.securitycustom.repositories.UserRepository;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public HomeController(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/")
    public String home(
            Authentication authentication,
            Model model
    ) {
        User user = findAuthenticatedUser(
                authentication
        );

        model.addAttribute(
                "profile",
                userMapper.toDto(user)
        );

        return "home";
    }

    @GetMapping("/admin")
    public String admin(
            Authentication authentication,
            Model model
    ) {
        User user = findAuthenticatedUser(
                authentication
        );

        model.addAttribute(
                "profile",
                userMapper.toDto(user)
        );

        return "admin/dashboard";
    }

    private User findAuthenticatedUser(
            Authentication authentication
    ) {
        return userRepository
                .findByUsernameIgnoreCase(
                        authentication.getName()
                )
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Không tìm thấy người dùng đăng nhập"
                        )
                );
    }
}