package com.fpt.servicecontract.contract.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

  private final Cloudinary cloudinary;

  @Override
  public String uploadImage(MultipartFile file) throws IOException {
    Map options = ObjectUtils.asMap("public_id", "IMAGE_" + UUID.randomUUID());
    Map result = cloudinary.uploader().upload(file.getBytes(), options);
    return (String) result.get("url");
  }

  @Override
  public String uploadPdf(File file) throws IOException {
    Map options = ObjectUtils.asMap("public_id", "PDF_" + UUID.randomUUID());
    Map result = cloudinary.uploader().upload(file, options);
    return (String) result.get("url");
  }

}
