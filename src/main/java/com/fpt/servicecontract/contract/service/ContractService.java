package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractDto;
import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SearchContractRequest;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest) throws Exception;
}
