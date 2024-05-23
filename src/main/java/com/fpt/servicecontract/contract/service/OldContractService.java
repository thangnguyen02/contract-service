package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.contract.model.OldContract;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface OldContractService {
    Page<OldContract> getContracts(Pageable pageable);
    BaseResponse create(String token, CreateUpdateOldContract request, MultipartFile[] images);
    String delete(String contractId);
}
