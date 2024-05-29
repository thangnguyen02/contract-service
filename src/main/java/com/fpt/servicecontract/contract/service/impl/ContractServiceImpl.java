package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.model.ContractParty;
import com.fpt.servicecontract.contract.repository.ContractPartyRepository;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.DateUltil;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
                .name(contractRequest.getPartyA().getName())
                .phone(contractRequest.getPartyA().getPhone())
                .position(contractRequest.getPartyA().getPosition())
                .identificationNumber(contractRequest.getPartyA().getIdentificationNumber())
                .dob(DateUltil.stringToDate(contractRequest.getPartyA().getDob(), DateUltil.DATE_FORMAT_dd_MM_yyyy))
                .gender(contractRequest.getPartyA().isGender())
                .address(contractRequest.getPartyA().getAddress())
                .build();
        ContractParty contractPartyB = ContractParty
                .builder()
                .name(contractRequest.getPartyB().getName())
                .phone(contractRequest.getPartyB().getPhone())
                .position(contractRequest.getPartyB().getPosition())
                .identificationNumber(contractRequest.getPartyB().getIdentificationNumber())
                .dob(DateUltil.stringToDate(contractRequest.getPartyB().getDob(), DateUltil.DATE_FORMAT_dd_MM_yyyy))
                .gender(contractRequest.getPartyB().isGender())
                .address(contractRequest.getPartyB().getAddress())
                .build();
        contractPartyB = contractPartyRepository.save(contractPartyB);
        contractPartyA = contractPartyRepository.save(contractPartyA);
        Contract contract  = Contract
                .builder()
                .contractName(contractRequest.getContractName())
                .contractNumber(contractRequest.getContractNumber())
                .description(contractRequest.getDescription())
                .term(contractRequest.getTerm())
                .content(contractRequest.getContent())
                .partyAId(contractPartyA.getId())
                .partyBId(contractPartyB.getId())
                .createdBy("token")
                .file("file")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        Context context = new Context();
//        context.setVariable("images", imageList);
        String html = pdfUtils.templateEngine().process("templates/new_contract.html", context);
        File file = pdfUtils.generatePdf(html, contract.getContractName() + "_" + UUID.randomUUID());
        contract.setFile(cloudinaryService.uploadPdf(file));


        return null;
    }
}
