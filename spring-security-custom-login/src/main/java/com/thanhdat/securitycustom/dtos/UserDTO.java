package com.thanhdat.securitycustom.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String fullName;

    private String images;

    private boolean enabled;

    private String roleName;
}