package com.fpt.servicecontract.contract.service;


import co.elastic.clients.elasticsearch.xpack.usage.Base;
import com.fpt.servicecontract.contract.dto.ContractTemplateRequest;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface ContractTemplateService {
    BaseResponse finAllTemplates(Pageable pageable);

    BaseResponse createContract(ContractTemplateRequest contractRequest);

    BaseResponse delete(String id);

    BaseResponse update(String id, ContractTemplateRequest contractRequest);
}
