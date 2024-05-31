package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest, String email) throws Exception;

    BaseResponse findAll(Pageable p);
}
