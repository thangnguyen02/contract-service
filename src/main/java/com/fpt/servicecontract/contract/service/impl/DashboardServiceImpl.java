package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.StatisticalDto;
import com.fpt.servicecontract.contract.model.ContractStatus;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.repository.ContractStatusRepository;
import com.fpt.servicecontract.contract.repository.OldContractRepository;
import com.fpt.servicecontract.contract.service.DashboardService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final ContractRepository contractRepository;
    private final OldContractRepository oldContractRepository;
    private final ContractStatusRepository contractStatusRepository;

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

    @Override
    public BaseResponse getNumberSale(String email, String status) {
        List<String> ids = contractStatusRepository.findAll().stream()
                .filter(m -> !ObjectUtils.isEmpty(m.getReceiver()) && m.getReceiver().contains(email) || !ObjectUtils.isEmpty(m.getSender()) && m.getSender().equals(email))
                .map(ContractStatus::getContractId)
                .toList();
        if (DataUtil.isNullObject(ids)) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not have any contract", false, 0);
        }
        Integer number = contractRepository.getNumberContractBySale(email, ids, status);
        if (number == null || number == 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not have any contract", false, 0);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Number contract exist", false, number);
    }

    @Override
    public BaseResponse countReason(String email, int sale, int number) {
        List<Object[]> list = contractRepository.countReason(email, number);
        List<Map<String, String>> mapArrayList = new ArrayList<>();
        for (Object[] obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("numberOfRejected", String.valueOf(obj[0]));
            map.put("reasonTitle", String.valueOf(obj[1]));
            mapArrayList.add(map);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, mapArrayList);
    }

    @Override
    public BaseResponse countTopSale() {
        List<Object[]> list = contractRepository.countTopSale();
        List<Map<String, String>> mapArrayList = new ArrayList<>();
        for (Object[] obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("numberOfRejected", String.valueOf(obj[0]));
            map.put("reasonTitle", String.valueOf(obj[1]));
            mapArrayList.add(map);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, mapArrayList);
    }

    @Override
    public BaseResponse contractSuccess() {
        List<Object[]> list = contractRepository.contractSuccess();
        List<Map<String, String>> mapArrayList = new ArrayList<>();
        for (Object[] obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("numberOfSuccess", String.valueOf(obj[0]));
            map.put("createBy", String.valueOf(obj[1]));
            map.put("name", String.valueOf(obj[2]));
            mapArrayList.add(map);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, mapArrayList);
    }

    @Override
    public BaseResponse totalContractCejected() {
        List<Object[]> list = contractRepository.totalContractCejected();
        List<Map<String, String>> mapArrayList = new ArrayList<>();
        for (Object[] obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("totalNumberOfRejected", String.valueOf(obj[0]));
            map.put("reasonTitle", String.valueOf(obj[1]));
            map.put("userIsRejectedThemost", String.valueOf(obj[2]));
            map.put("nameUserIsRejectedThemost", String.valueOf(obj[3]));
            mapArrayList.add(map);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, mapArrayList);
    }

}
