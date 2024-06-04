package com.fpt.servicecontract.contract.service;


import com.fpt.servicecontract.contract.dto.ContractHistoryDto;
import com.fpt.servicecontract.utils.BaseResponse;

import java.util.List;

public interface ContractHistoryService {

    void createContractHistory(String contractId, String createdBy, String status);

    BaseResponse getContractHistory(String contractId);
}
