package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OldContractService {

    BaseResponse getContracts(int page, int size);

    BaseResponse create(String token, CreateUpdateOldContract request, MultipartFile[] images) throws Exception;

    BaseResponse delete(String contractId) throws IOException;

    Void sync();
}
