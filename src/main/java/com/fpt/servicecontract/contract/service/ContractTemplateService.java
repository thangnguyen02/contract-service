package com.fpt.servicecontract.contract.service;


import com.fpt.servicecontract.contract.dto.request.ContractTemplateRequest;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface ContractTemplateService {
    BaseResponse finAllTemplates(Pageable pageable, String contractName);

    BaseResponse createContract(ContractTemplateRequest contractRequest);

    BaseResponse delete(String id);

    BaseResponse update(String id, ContractTemplateRequest contractRequest);
}
