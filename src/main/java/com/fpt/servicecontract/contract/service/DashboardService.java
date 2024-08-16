package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.utils.BaseResponse;

import java.util.Date;

public interface DashboardService {

    BaseResponse numberNewContract(Date fromDate, Date toDate, String signStatus);

    BaseResponse getNumberSale(String email, String status);

    BaseResponse countReason(String email, int sale, int number);

    BaseResponse countTopSale();

    BaseResponse contractSuccess();

    BaseResponse totalContractCejected();
}
