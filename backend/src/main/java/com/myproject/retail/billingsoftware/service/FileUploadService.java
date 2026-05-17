package com.myproject.retail.billingsoftware.service;

import org.springframework.web.multipart.MultipartFile;
import com.myproject.retail.billingsoftware.io.CloudinaryResponse;

public interface FileUploadService {

    CloudinaryResponse uploadFile(MultipartFile file);

    boolean deleteFile(String publicId);
}