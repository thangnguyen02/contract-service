package com.fpt.servicecontract.contract.service;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContractService {
    public String scanContract(List<MultipartFile> images) throws IOException, TesseractException;
}
