package com.fpt.servicecontract.contract.service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
  public String uploadImage(MultipartFile multipartFile) throws IOException;

  public String uploadPdf(File file) throws IOException;
}
