package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.model.ContractParty;
import com.fpt.servicecontract.contract.repository.ContractPartyRepository;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DateUltil;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractPartyRepository contractPartyRepository;
    private final PdfUtils pdfUtils;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public BaseResponse createContract(ContractRequest contractRequest) throws Exception {
        ContractParty contractPartyA = ContractParty
                .builder()
                .address(contractRequest.getPartyA().getAddress())
                .name(contractRequest.getPartyA().getName())
                .taxNumber(contractRequest.getPartyA().getTaxNumber())
                .presenter(contractRequest.getPartyA().getPresenter())
                .businessNumber(contractRequest.getPartyA().getBusinessNumber())
                .bankId(contractRequest.getPartyA().getBankId())
                .bankName(contractRequest.getPartyA().getBankName())
                .bankAccOwer(contractRequest.getPartyA().getBankAccOwer())
                .email(contractRequest.getPartyA().getEmail())
                .position(contractRequest.getPartyA().getPosition())
                .build();
        ContractParty contractPartyB = ContractParty
                .builder()
                .address(contractRequest.getPartyB().getAddress())
                .name(contractRequest.getPartyB().getName())
                .taxNumber(contractRequest.getPartyB().getTaxNumber())
                .presenter(contractRequest.getPartyB().getPresenter())
                .businessNumber(contractRequest.getPartyB().getBusinessNumber())
                .bankId(contractRequest.getPartyB().getBankId())
                .bankName(contractRequest.getPartyB().getBankName())
                .bankAccOwer(contractRequest.getPartyB().getBankAccOwer())
                .email(contractRequest.getPartyB().getEmail())
                .position(contractRequest.getPartyB().getPosition())
                .build();
        contractPartyB = contractPartyRepository.save(contractPartyB);
        contractPartyA = contractPartyRepository.save(contractPartyA);
        Contract contract  = Contract
                .builder()
                .contractName(contractRequest.getContractName())
                .contractNumber(contractRequest.getContractNumber())
                .rule(contractRequest.getRule())
                .term(contractRequest.getTerm())
                .partyAId(contractPartyA.getId())
                .partyBId(contractPartyB.getId())
                .createdBy("token")
                .file("file")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        Context context = new Context();
        context.setVariable("partyA", contractPartyA);
        context.setVariable("partyB", contractPartyB);
        context.setVariable("info", contract);
        context.setVariable("date", LocalDateTime.now().toLocalDate());
        String html = pdfUtils.templateEngine().process("templates/new_contract.html", context);
        File file = pdfUtils.generatePdf(html, contract.getContractName() + "_" + UUID.randomUUID());
        contract.setFile(cloudinaryService.uploadPdf(file));
        Contract res = contractRepository.save(contract);

        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Ok", true, res);
    }

    @Override
    public BaseResponse findAll() {
        List<Contract> contractList = contractRepository.findAllC();
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, contractList);
    }
}
