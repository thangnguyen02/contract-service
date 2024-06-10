package com.fpt.servicecontract.contract.service;

import java.util.List;

public interface ContractStatusService {
    void create(String sender, List<String> to, String contractId);
}
