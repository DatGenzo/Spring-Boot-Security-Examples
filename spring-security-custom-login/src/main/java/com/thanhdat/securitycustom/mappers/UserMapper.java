package com.thanhdat.securitycustom.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.thanhdat.securitycustom.dtos.UserDTO;
import com.thanhdat.securitycustom.entities.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(
            target = "roleName",
            source = "role.name"
    )
    UserDTO toDto(User user);
}