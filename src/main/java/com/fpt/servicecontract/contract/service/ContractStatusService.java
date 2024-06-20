package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.ContractStatus;

import java.util.List;

public interface ContractStatusService {
    void create(String sender, List<String> to, String contractId, String status, String description);
    String getContractStatusByLastStatus(String contractId);
}
