package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.PaySlipFormula;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface PaySlipFormulaService {
    BaseResponse getAll(Pageable pageable);
    BaseResponse getById(String id);
    BaseResponse create(PaySlipFormula paySlipFormula);
    BaseResponse update(String id, PaySlipFormula paySlipFormula);
    BaseResponse delete(String id);
}
