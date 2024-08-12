package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractHistoryDto;
import com.fpt.servicecontract.contract.model.ContractHistory;
import com.fpt.servicecontract.contract.repository.ContractHistoryRepository;
import com.fpt.servicecontract.contract.service.ContractHistoryService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractHistoryServiceImpl implements ContractHistoryService {

    private final ContractHistoryRepository repository;

    @Override
    public void createContractHistory(String contractId, String contractName, String createdBy, String comment, String status, String reasonId) {
        ContractHistory contractHistory = ContractHistory.builder()
                .contractId(contractId)
                .createdBy(createdBy)
                .contractName(contractName)
                .status(status)
                .createdDate(LocalDateTime.now())
                .comment(comment)
                .reasonId(reasonId)
                .build();
        repository.save(contractHistory);
    }

    @Override
    public BaseResponse getContractHistory(String contractId, String createdBy) {
        List<Object[]> result = repository.getContractHistoriesByContractId(contractId, createdBy);
        if (result.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "The history is empty", true, null);
        }
        List<ContractHistoryDto> response = new ArrayList<>();
        for (Object[] row : result) {
            response.add(ContractHistoryDto.builder()
                    .id(row[0].toString())
                    .contractId(row[1].toString())
                    .createdBy(row[2].toString())
                    .time(row[3].toString())
                    .status(row[4].toString())
                    .contractName(row[5].toString())
                    .comment(row[6].toString())
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, response);
    }


}
