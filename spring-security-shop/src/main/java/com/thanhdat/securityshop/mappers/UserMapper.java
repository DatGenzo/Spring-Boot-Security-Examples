package com.thanhdat.securityshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.thanhdat.securityshop.dtos.UserDTO;
import com.thanhdat.securityshop.entities.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "productCount", ignore = true)
    UserDTO toDto(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserDTO dto, @MappingTarget User user);
}