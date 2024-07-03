package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.contract.repository.ContractAppendicesRepository;
import com.fpt.servicecontract.contract.service.ContractAppendicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractAppendicesServiceImpl implements ContractAppendicesService {

    private final ContractAppendicesRepository repository;

    public List<ContractAppendices> getAll() {
        return repository.findAll();
    }

    public Optional<ContractAppendices> getById(String id) {
        return repository.findById(id);
    }

    public ContractAppendices save(ContractAppendices contractAppendices) {
        return repository.save(contractAppendices);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
