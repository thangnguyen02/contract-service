package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.ContractType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ContractTypeService {
    Page<ContractType> getAllContractTypes(int page, int size, String name);

    Optional<ContractType> getContractTypeById(String id);

    ContractType createContractType(ContractType contractType);

    ContractType updateContractType(String id, ContractType contractTypeDetails);

    void deleteContractType(String id);
}
