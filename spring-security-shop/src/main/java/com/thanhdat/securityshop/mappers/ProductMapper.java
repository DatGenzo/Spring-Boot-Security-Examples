package com.thanhdat.securityshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.thanhdat.securityshop.dtos.ProductDTO;
import com.thanhdat.securityshop.entities.Product;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "image", ignore = true)
    ProductDTO toDto(Product product);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductDTO dto);
}