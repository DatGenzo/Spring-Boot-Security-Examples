package com.thanhdat.securityemail.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({
            "/",
            "/home"
    })
    public String showHome() {
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin-dashboard";
    }
}
