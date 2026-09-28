package com.thanhdat.securityshop.controllers;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thanhdat.securityshop.dtos.UserDTO;
import com.thanhdat.securityshop.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("users", userService.findAll(keyword, page, size));
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "users/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        UserDTO dto = new UserDTO();
        dto.setEnabled(true);
        dto.setRoleName("ROLE_USER");
        prepareForm(model, dto, true);
        return "users/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("userDTO") UserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirect
    ) {
        if (result.hasErrors()) {
            prepareForm(model, dto, true);
            return "users/form";
        }

        try {
            userService.create(dto);
            redirect.addFlashAttribute(
                    "success",
                    "Tạo User thành công. Mật khẩu mặc định: 123456"
            );
            return "redirect:/users";
        } catch (IllegalArgumentException exception) {
            result.reject("user.error", exception.getMessage());
            prepareForm(model, dto, true);
            return "users/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        prepareForm(model, userService.findById(id), false);
        return "users/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("userDTO") UserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirect
    ) {
        dto.setId(id);

        if (result.hasErrors()) {
            prepareForm(model, dto, false);
            return "users/form";
        }

        try {
            userService.update(id, dto);
            redirect.addFlashAttribute("success", "Cập nhật User thành công");
            return "redirect:/users";
        } catch (IllegalArgumentException exception) {
            result.reject("user.error", exception.getMessage());
            prepareForm(model, dto, false);
            return "users/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirect
    ) {
        try {
            userService.delete(id);
            redirect.addFlashAttribute("success", "Xóa User thành công");
        } catch (IllegalArgumentException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/users";
    }

    private void prepareForm(Model model, UserDTO dto, boolean creating) {
        model.addAttribute("userDTO", dto);
        model.addAttribute("creating", creating);
    }
}