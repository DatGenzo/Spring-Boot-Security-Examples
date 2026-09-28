package com.thanhdat.securityshop.controllers;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thanhdat.securityshop.dtos.ProductDTO;
import com.thanhdat.securityshop.security.CustomUserDetails;
import com.thanhdat.securityshop.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("products", productService.findAll(keyword, page, size));
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "products/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        prepareForm(model, new ProductDTO(), true);
        return "products/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("productDTO") ProductDTO dto,
            BindingResult result,
            @RequestParam(name = "image", required = false) MultipartFile image,
            Authentication authentication,
            Model model,
            RedirectAttributes redirect
    ) {
        if (result.hasErrors()) {
            prepareForm(model, dto, true);
            return "products/form";
        }

        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            productService.create(dto, image, principal.getId());
            redirect.addFlashAttribute("success", "Tạo Product thành công");
            return "redirect:/products";
        } catch (IllegalArgumentException | IllegalStateException exception) {
            result.reject("product.error", exception.getMessage());
            prepareForm(model, dto, true);
            return "products/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        prepareForm(model, productService.findById(id), false);
        return "products/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("productDTO") ProductDTO dto,
            BindingResult result,
            @RequestParam(name = "image", required = false) MultipartFile image,
            Model model,
            RedirectAttributes redirect
    ) {
        dto.setId(id);

        if (result.hasErrors()) {
            ProductDTO existing = productService.findById(id);
            dto.setImageUrl(existing.getImageUrl());
            prepareForm(model, dto, false);
            return "products/form";
        }

        try {
            productService.update(id, dto, image);
            redirect.addFlashAttribute("success", "Cập nhật Product thành công");
            return "redirect:/products";
        } catch (IllegalArgumentException | IllegalStateException exception) {
            result.reject("product.error", exception.getMessage());
            ProductDTO existing = productService.findById(id);
            dto.setImageUrl(existing.getImageUrl());
            prepareForm(model, dto, false);
            return "products/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirect
    ) {
        try {
            productService.delete(id);
            redirect.addFlashAttribute("success", "Xóa Product thành công");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/products";
    }

    private void prepareForm(Model model, ProductDTO dto, boolean creating) {
        model.addAttribute("productDTO", dto);
        model.addAttribute("creating", creating);
    }
}