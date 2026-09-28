package com.thanhdat.securityshop.services.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.thanhdat.securityshop.services.CloudinaryService;
import com.thanhdat.securityshop.services.CloudinaryUploadResult;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryUploadResult upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Chưa chọn ảnh");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Chỉ chấp nhận JPG, PNG hoặc WEBP");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "security-shop/products",
                            "resource_type", "image"
                    )
            );

            return new CloudinaryUploadResult(
                    String.valueOf(result.get("secure_url")),
                    String.valueOf(result.get("public_id"))
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Upload Cloudinary thất bại", exception);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    publicId,
                    Map.of("resource_type", "image")
            );
        } catch (Exception exception) {
            throw new IllegalStateException("Xóa ảnh Cloudinary thất bại", exception);
        }
    }
}