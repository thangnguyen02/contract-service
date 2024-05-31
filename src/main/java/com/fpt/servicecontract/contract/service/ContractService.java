package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.utils.BaseResponse;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest) throws Exception;

    BaseResponse findAll();
}
