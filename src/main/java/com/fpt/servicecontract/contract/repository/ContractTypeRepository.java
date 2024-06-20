package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContractTypeRepository  extends PagingAndSortingRepository<ContractType, String> {
    Optional<ContractType> findById(String id);

    ContractType save(ContractType contractType);

    void deleteById(String id);
}
