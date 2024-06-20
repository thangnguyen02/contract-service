package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.ContractStatus;
import com.fpt.servicecontract.contract.repository.ContractStatusRepository;
import com.fpt.servicecontract.contract.service.ContractStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractStatusServiceImpl implements ContractStatusService {
    private final ContractStatusRepository contractStatusRepository;

    @Override
    public void create(String sender, List<String> receivers, String contractId, String status, String description) {
        ContractStatus contractStatus = ContractStatus
                .builder()
                .sender(sender)
                .receiver(receivers)
                .contractId(contractId)
                .sendDate(LocalDateTime.now())
                .status(status)
                .description(description)
                .build();
        contractStatusRepository.save(contractStatus);
    }

    @Override
    public String getContractStatusByLastStatus(String contractId) {
        String contractStatus = contractStatusRepository.findByContractLastStatus(contractId);
        return contractStatus;
    }

}
