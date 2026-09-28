package com.thanhdat.securityshop.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username không được để trống")
    @Size(max = 50, message = "Username tối đa 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 150, message = "Email tối đa 150 ký tự")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 200, message = "Họ tên tối đa 200 ký tự")
    private String fullName;

    private boolean enabled;

    private String roleName;

    private long productCount;
}