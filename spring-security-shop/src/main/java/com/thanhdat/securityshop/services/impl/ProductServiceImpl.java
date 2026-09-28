package com.thanhdat.securityshop.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thanhdat.securityshop.dtos.ProductDTO;
import com.thanhdat.securityshop.entities.Product;
import com.thanhdat.securityshop.entities.User;
import com.thanhdat.securityshop.mappers.ProductMapper;
import com.thanhdat.securityshop.repositories.ProductRepository;
import com.thanhdat.securityshop.repositories.UserRepository;
import com.thanhdat.securityshop.services.CloudinaryService;
import com.thanhdat.securityshop.services.CloudinaryUploadResult;
import com.thanhdat.securityshop.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    public ProductServiceImpl(
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductMapper productMapper,
            CloudinaryService cloudinaryService
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        return productRepository.search(normalize(keyword), pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productMapper.toDto(findEntity(id));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto, MultipartFile image, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        Product product = productMapper.toEntity(dto);
        product.setUser(user);

        if (hasFile(image)) {
            CloudinaryUploadResult uploaded = cloudinaryService.upload(image);
            product.setImageUrl(uploaded.url());
            product.setImagePublicId(uploaded.publicId());
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto, MultipartFile image) {
        Product product = findEntity(id);
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        if (hasFile(image)) {
            CloudinaryUploadResult uploaded = cloudinaryService.upload(image);
            String oldPublicId = product.getImagePublicId();

            product.setImageUrl(uploaded.url());
            product.setImagePublicId(uploaded.publicId());

            cloudinaryService.delete(oldPublicId);
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = findEntity(id);
        cloudinaryService.delete(product.getImagePublicId());
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }

    private Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));
    }

    private boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private String normalize(String keyword) {
        return keyword == null ? "" : keyword.trim();
    }
}