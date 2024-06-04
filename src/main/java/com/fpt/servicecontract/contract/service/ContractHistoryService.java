package com.fpt.servicecontract.contract.service;


import com.fpt.servicecontract.utils.BaseResponse;

public interface ContractHistoryService {

    void createContractHistory(String contractId,String contractName, String createdBy, String status);

    BaseResponse getContractHistory(String contractId);
}
