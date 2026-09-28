package com.thanhdat.securityshop.dtos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 500, message = "Tên sản phẩm tối đa 500 ký tự")
    private String name;

    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    private String imageUrl;
    private String imagePublicId;
    private Long userId;
    private String username;
    private MultipartFile image;
}