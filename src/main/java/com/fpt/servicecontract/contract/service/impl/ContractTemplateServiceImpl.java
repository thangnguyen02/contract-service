package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractTemplateDto;
import com.fpt.servicecontract.contract.dto.request.ContractTemplateRequest;
import com.fpt.servicecontract.contract.model.ContractTemplate;
import com.fpt.servicecontract.contract.repository.ContractTemplateRepository;
import com.fpt.servicecontract.contract.service.ContractTemplateService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DataUtil;
import com.fpt.servicecontract.utils.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractTemplateServiceImpl implements ContractTemplateService {

    private final ContractTemplateRepository contractTemplateRepository;

    @Override
    public BaseResponse finAllTemplates(Pageable p, String contractName) {
        Page<Object[]> page = contractTemplateRepository.findAllContractTemplate(p, QueryUtils.appendPercent(contractName));
        List<ContractTemplateDto> responses = new ArrayList<>();
        for (Object[] obj : page) {
            responses.add(ContractTemplateDto.builder()
                    .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .nameContract(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .numberContract(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .ruleContract(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .termContract(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .name(Objects.nonNull(obj[5]) ? obj[5].toString() : null)
                    .address(Objects.nonNull(obj[6]) ? obj[6].toString() : null)
                    .taxNumber(Objects.nonNull(obj[7]) ? obj[7].toString() : null)
                    .presenter(Objects.nonNull(obj[8]) ? obj[8].toString() : null)
                    .position(Objects.nonNull(obj[9]) ? obj[9].toString() : null)
                    .businessNumber(Objects.nonNull(obj[10]) ? obj[10].toString() : null)
                    .bankId(Objects.nonNull(obj[11]) ? obj[11].toString() : null)
                    .bankName(Objects.nonNull(obj[12]) ? obj[12].toString() : null)
                    .bankAccOwer(Objects.nonNull(obj[13]) ? obj[13].toString() : null)
                    .email(Objects.nonNull(obj[14]) ? obj[14].toString() : null)
                    .createdDate(Objects.nonNull(obj[15]) ? obj[15].toString() : null)
                    .updatedDate(Objects.nonNull(obj[16]) ? obj[16].toString() : null)
                    .build());
        }
        Page<ContractTemplateDto> result = new PageImpl<>(responses, p,
                page.getTotalElements());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, result);
    }

    @Override
    public BaseResponse createContract(ContractTemplateRequest contractRequest) {
        ContractTemplate template = new ContractTemplate();
        template.setNameContract(contractRequest.getNameContract());
        template.setNumberContract(contractRequest.getNumberContract());
        template.setRuleContract(contractRequest.getRuleContract());
        template.setTermContract(contractRequest.getTermContract());
        template.setName(contractRequest.getName());
        template.setAddress(contractRequest.getAddress());
        template.setTaxNumber(contractRequest.getTaxNumber());
        template.setPresenter(contractRequest.getPresenter());
        template.setPosition(contractRequest.getPosition());
        template.setBusinessNumber(contractRequest.getBusinessNumber());
        template.setBankId(contractRequest.getBankId());
        template.setBankName(contractRequest.getBankName());
        template.setBankAccOwer(contractRequest.getBankAccOwer());
        template.setEmail(contractRequest.getEmail());
        template.setCreatedDate(LocalDateTime.now());
        template.setMarkDeleted(false);
        try {
            contractTemplateRepository.save(template);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create successfully", true, ContractTemplateDto.builder()
                    .id(template.getId())
                    .nameContract(template.getNameContract())
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
        }
    }

    @Override
    public BaseResponse delete(String id) {
        var template = contractTemplateRepository.findById(id);
        if (template.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "the contract template not exist", true, null);
        }
        template.get().setMarkDeleted(true);
        try {
            contractTemplateRepository.save(template.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Successfully", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
        }
    }

    @Override
    public BaseResponse update(String id, ContractTemplateRequest contractRequest) {
        var templateOptional = contractTemplateRepository.findById(id);
        if (templateOptional.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "the contract template not exist", true, null);
        }
        var template = templateOptional.get();
        template.setNameContract (DataUtil.isNullObject(contractRequest.getNameContract()) ? null : contractRequest.getNameContract());
        template.setNumberContract(DataUtil.isNullObject(contractRequest.getNumberContract()) ? null : contractRequest.getNumberContract());
        template.setRuleContract(DataUtil.isNullObject(contractRequest.getRuleContract()) ? null : contractRequest.getRuleContract());
        template.setTermContract(DataUtil.isNullObject(contractRequest.getTermContract()) ? null : contractRequest.getTermContract());
        template.setName(DataUtil.isNullObject(contractRequest.getName()) ? null : contractRequest.getName());
        template.setAddress(DataUtil.isNullObject(contractRequest.getAddress()) ? null : contractRequest.getAddress());
        template.setTaxNumber(DataUtil.isNullObject(contractRequest.getTaxNumber()) ? null : contractRequest.getTaxNumber());
        template.setPresenter(DataUtil.isNullObject(contractRequest.getPresenter()) ? null : contractRequest.getPresenter());
        template.setPosition(DataUtil.isNullObject(contractRequest.getPosition()) ? null : contractRequest.getPosition());
        template.setBusinessNumber(DataUtil.isNullObject(contractRequest.getBusinessNumber()) ? null : contractRequest.getBusinessNumber());
        template.setBankId(DataUtil.isNullObject(contractRequest.getBankId()) ? null:contractRequest.getBankId());
        template.setBankName(DataUtil.isNullObject(contractRequest.getBankName()) ? null : contractRequest.getBankName());
        template.setBankAccOwer(DataUtil.isNullObject(contractRequest.getBankAccOwer()) ? null : contractRequest.getBankAccOwer());
        template.setEmail(DataUtil.isNullObject(contractRequest.getEmail()) ? null : contractRequest.getEmail());
        template.setUpdatedDate(LocalDateTime.now());
        try {
            contractTemplateRepository.save(template);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Update Successfully", true, ContractTemplateDto.builder()
                    .id(template.getId())
                    .nameContract(template.getNameContract())
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
        }
    }
}
