package com.thanhdat.securityshop.services;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.thanhdat.securityshop.dtos.ProductDTO;

public interface ProductService {

    Page<ProductDTO> findAll(String keyword, int page, int size);

    ProductDTO findById(Long id);

    ProductDTO create(ProductDTO dto, MultipartFile image, Long userId);

    ProductDTO update(Long id, ProductDTO dto, MultipartFile image);

    void delete(Long id);

    long countProducts();
}