package com.thanhdat.securityshop.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    CloudinaryUploadResult upload(MultipartFile file);

    void delete(String publicId);
}