package com.myproject.retail.billingsoftware.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myproject.retail.billingsoftware.io.CloudinaryResponse;
import com.myproject.retail.billingsoftware.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    public FileUploadServiceImpl(
            @Value("${cloudinary.cloud.name}") String cloudName,
            @Value("${cloudinary.api.key}") String apiKey,
            @Value("${cloudinary.api.secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public CloudinaryResponse uploadFile(MultipartFile file) {
        try {
        	@SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "billing-app")
            );

            return new CloudinaryResponse(
                    result.get("secure_url").toString(),
                    result.get("public_id").toString()
            );

        } catch (Exception e) {
            log.error("Failed to upload file to Cloudinary", e);
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public boolean deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equalsIgnoreCase(String.valueOf(result.get("result")));
        } catch (Exception e) {
            log.error("Failed to delete file from Cloudinary: {}", publicId, e);
            return false;
        }
    }
    
    
}