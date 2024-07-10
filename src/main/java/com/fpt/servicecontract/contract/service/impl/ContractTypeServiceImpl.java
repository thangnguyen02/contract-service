package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.ContractType;
import com.fpt.servicecontract.contract.repository.ContractTypeRepository;
import com.fpt.servicecontract.contract.service.ContractTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractTypeServiceImpl implements ContractTypeService {

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Override
    public Page<ContractType> getAllContractTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contractTypeRepository.findAll(pageable);
    }

    public Optional<ContractType> getContractTypeById(String id) {
        return contractTypeRepository.findById(id);
    }

    public ContractType createContractType(ContractType contractType) {
        return contractTypeRepository.save(contractType);
    }

    public ContractType updateContractType(String id, ContractType contractTypeDetails) {
        Optional<ContractType> contractTypeOptional = contractTypeRepository.findById(id);

        if (contractTypeOptional.isPresent()) {
            ContractType contractType = contractTypeOptional.get();
            contractType.setTitle(contractTypeDetails.getTitle());
            contractType.setDescription(contractTypeDetails.getDescription());
            contractType.setMarkDeleted(contractTypeDetails.getMarkDeleted());
            return contractTypeRepository.save(contractType);
        } else {
            throw new RuntimeException("ContractType not found with id " + id);
        }
    }

    public void deleteContractType(String id) {
        contractTypeRepository.deleteById(id);
    }
}
