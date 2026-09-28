package com.thanhdat.securityemail.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.thanhdat.securityemail.dtos.UserDTO;
import com.thanhdat.securityemail.entities.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy =
                ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(
            target = "roleId",
            source = "role.id"
    )
    @Mapping(
            target = "roleName",
            source = "role.name"
    )
    UserDTO toDTO(User user);
}
