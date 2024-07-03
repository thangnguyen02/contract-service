package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.ContractAppendices;

import java.util.List;
import java.util.Optional;

public interface ContractAppendicesService {
    List<ContractAppendices> getAll();

    Optional<ContractAppendices> getById(String id);

    ContractAppendices save(ContractAppendices contractAppendices);

    void deleteById(String id);
}
