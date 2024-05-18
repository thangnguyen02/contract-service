package com.fpt.servicecontract.contract.service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
  public String uploadMultipartFile(MultipartFile multipartFile) throws IOException;

  public String uploadFile(File file) throws IOException;
}
