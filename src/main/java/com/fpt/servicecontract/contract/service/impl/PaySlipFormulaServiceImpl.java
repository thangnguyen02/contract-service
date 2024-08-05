package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.request.PaySlipFormulaUpdateRequest;
import com.fpt.servicecontract.contract.model.PaySlipFormula;
import com.fpt.servicecontract.contract.repository.PaySlipFormulaRepository;
import com.fpt.servicecontract.contract.service.PaySlipFormulaService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaySlipFormulaServiceImpl implements PaySlipFormulaService {
    private final PaySlipFormulaRepository paySlipFormulaRepository;


    @Override
    public BaseResponse getAll(Pageable pageable) {
        Page<PaySlipFormula> paySlipFormulas = paySlipFormulaRepository.findAll(pageable);
        if (paySlipFormulas.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "Not have any pay slip formula", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "successfully", true, paySlipFormulas);
    }

    @Override
    public BaseResponse getById(String id) {
        var paySlipFormulas = paySlipFormulaRepository.findById(id);
        if (paySlipFormulas.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "Not have any pay slip formula", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "successfully", true, paySlipFormulas.get());
    }

    @Override
    public BaseResponse create(PaySlipFormula paySlipFormula) {
        try {
            var newPaySlipFormula = paySlipFormulaRepository.save(paySlipFormula);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "create successfully", true, newPaySlipFormula);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Can not create pay slip formula", true, null);
        }
    }

    @Override
    public BaseResponse update(List<PaySlipFormulaUpdateRequest> paySlipFormula) {
        List<String> deleteId = new ArrayList<>();
        List<PaySlipFormula> createdOrUpdatedPaySlipFormulas = new ArrayList<>();
        for (PaySlipFormulaUpdateRequest item : paySlipFormula) {
            if ("INACTIVE".equals(item.getStatus())) {
                deleteId.add(item.getId());
            } else {
                PaySlipFormula paySlipFormula1 = new PaySlipFormula();
                BeanUtils.copyProperties(item, paySlipFormula1);
                createdOrUpdatedPaySlipFormulas.add(paySlipFormula1);
            }
        }
        try {
            paySlipFormulaRepository.saveAll(createdOrUpdatedPaySlipFormulas);
            paySlipFormulaRepository.deleteAllById(deleteId);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "update successfully", true, newPaySlipFormula);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Can not update pay slip formula", true, null);
        }
    }

    @Override
    public BaseResponse delete(String id) {
        var paySlipFormula = paySlipFormulaRepository.findById(id);
        if (paySlipFormula.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "pay slip formula not exist", true, null);
        }
        try {
            paySlipFormulaRepository.delete(paySlipFormula.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "delete successfully", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Can not delete pay slip formula", true, null);
        }
    }
}
