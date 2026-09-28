package com.thanhdat.securityemail.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private String fullName;
    private Long roleId;
    private String roleName;
    private boolean enabled;
    private LocalDateTime createdAt;
}
