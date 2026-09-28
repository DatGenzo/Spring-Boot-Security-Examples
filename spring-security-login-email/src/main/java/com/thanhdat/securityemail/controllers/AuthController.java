package com.thanhdat.securityemail.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }
}
