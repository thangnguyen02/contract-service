package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.utils.BaseResponse;

public interface PaySlipService {
    BaseResponse CalculateAllPaySlip();
    BaseResponse GetAllPaySlip();
}
