package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaySlipService {
    BaseResponse CalculateAllPaySlip();
    BaseResponse GetAllPaySlip(Pageable pageable, Integer month, Integer year);
    BaseResponse GetPaySlipById(Pageable pageable, Integer month, Integer year, String email);
    BaseResponse GetCommission();
}
