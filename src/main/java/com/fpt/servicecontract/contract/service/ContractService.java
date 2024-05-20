package com.fpt.servicecontract.contract.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContractService {
    public String createOldContract(String content ,List<MultipartFile> images) ;
}
