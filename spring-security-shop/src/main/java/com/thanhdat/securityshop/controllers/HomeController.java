package com.thanhdat.securityshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thanhdat.securityshop.services.ProductService;
import com.thanhdat.securityshop.services.UserService;

@Controller
public class HomeController {

    private final UserService userService;
    private final ProductService productService;

    public HomeController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("userCount", userService.countUsers());
        model.addAttribute("productCount", productService.countProducts());
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}