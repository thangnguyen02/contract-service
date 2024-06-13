package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.StatisticalDto;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.repository.OldContractRepository;
import com.fpt.servicecontract.contract.service.DashboardService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final ContractRepository contractRepository;
    private final OldContractRepository oldContractRepository;

    @Override
    public BaseResponse numberNewContract(Date fromDate, Date toDate, String signStatus) {
        Integer oldContract = oldContractRepository.staticalOldContract(fromDate, toDate);
        Integer newContract = contractRepository.staticalNewContract(fromDate, toDate);

        Integer resultByMonth = newContract + oldContract;
        Integer resultBySignStatus = contractRepository.statisticSignStatus(signStatus);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Statistical successfully", true, StatisticalDto.builder()
                .resultByMonth(resultByMonth)
                .resultBySignStatus(resultBySignStatus)
                .build());
    }



}
