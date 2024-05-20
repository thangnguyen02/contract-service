package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    @Override
    public String createOldContract(String content, List<MultipartFile> images) {
        return "ok";
    }
}
