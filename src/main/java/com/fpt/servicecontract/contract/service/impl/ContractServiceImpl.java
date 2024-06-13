package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.ContractResponse;
import com.fpt.servicecontract.contract.dto.PartyRequest;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.model.ContractParty;
import com.fpt.servicecontract.contract.model.ContractStatus;
import com.fpt.servicecontract.contract.repository.ContractPartyRepository;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.repository.ContractStatusRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.contract.service.ContractHistoryService;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.PdfUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractPartyRepository contractPartyRepository;
    private final PdfUtils pdfUtils;
    private final CloudinaryService cloudinaryService;
    private final ContractHistoryService contractHistoryService;
    private final ElasticSearchService elasticSearchService;
    private final ContractStatusRepository contractStatusRepository;

    @Override
    public BaseResponse createContract(ContractRequest contractRequest, String email) throws Exception {
        ContractParty contractPartyA = ContractParty
                .builder()
                .id(contractRequest.getPartyA().getId())
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
                .id(contractRequest.getPartyB().getId())
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
        try {
            contractPartyB = contractPartyRepository.save(contractPartyB);
            contractPartyA = contractPartyRepository.save(contractPartyA);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Failed", false, e.getMessage());
        }
        Contract contract = Contract
                .builder()
                .id(contractRequest.getId())
                .name(contractRequest.getName())
                .number(contractRequest.getNumber())
                .rule(contractRequest.getRule())
                .createdBy(email)
                .term(contractRequest.getTerm())
                .partyAId(contractPartyA.getId())
                .partyBId(contractPartyB.getId())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .status(Constants.STATUS.NEW)
                .isUrgent(contractRequest.isUrgent())
                .build();
        Context context = new Context();
        context.setVariable("partyA", contractPartyA);
        context.setVariable("partyB", contractPartyB);
        context.setVariable("info", contract);
        context.setVariable("date", LocalDateTime.now().toLocalDate());
        String html = pdfUtils.templateEngine().process("templates/new_contract.html", context);
        File file = pdfUtils.generatePdf(html, contract.getName() + "_" + UUID.randomUUID());
        contract.setFile(cloudinaryService.uploadPdf(file));
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.warn("Failed to delete the file: {}", file.getAbsolutePath());
            }
        }
        if (contractRequest.getId() == null) {
            contract.setStatus(Constants.STATUS.NEW);
            contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), "", Constants.STATUS.NEW);
        } else {
            contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), "", Constants.STATUS.UPDATE);
        }
        Contract result = contractRepository.save(contract);
        contractRequest.setId(result.getId());
        elasticSearchService.indexDocument("contract", contractRequest, ContractRequest::getId);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Successfully", true, contract);
    }

    @Override
    public BaseResponse findAll(Pageable p, String email) {
        List<String> ids = contractStatusRepository.findAll().stream()
                .filter(m -> m.getReceiver().contains(email) || m.getSender().equals(email))
                .map(ContractStatus::getContractId)
                .toList();
        Page<Object[]> page = contractRepository.findAllContract(p, email, ids);
        List<ContractResponse> responses = new ArrayList<>();
        for (Object[] obj : page) {
            responses.add(ContractResponse.builder()
                    .name(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .createdBy(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .file(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .createdDate(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .id(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .status(Objects.nonNull(obj[5]) ? obj[5].toString() : null)
                    .isUrgent(Objects.nonNull(obj[6]) && Boolean.parseBoolean(obj[6].toString()))
                    .build());
        }
        Page<ContractResponse> result = new PageImpl<>(responses, p,
                page.getTotalElements());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, result);
    }

    @Override
    public ContractRequest findById(String id) {
        List<Object[]> lst = contractRepository.findByIdContract(id);
        ContractRequest contractRequest = new ContractRequest();
        for (Object[] obj : lst) {
            contractRequest = ContractRequest.builder()
                    .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .name(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .number(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .rule(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .term(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .partyA(PartyRequest.builder()
                            .id(Objects.nonNull(obj[5]) ? obj[5].toString() : null)
                            .name(Objects.nonNull(obj[6]) ? obj[6].toString() : null)
                            .address(Objects.nonNull(obj[7]) ? obj[7].toString() : null)
                            .taxNumber(Objects.nonNull(obj[8]) ? obj[8].toString() : null)
                            .presenter(Objects.nonNull(obj[9]) ? obj[9].toString() : null)
                            .position(Objects.nonNull(obj[10]) ? obj[10].toString() : null)
                            .businessNumber(Objects.nonNull(obj[11]) ? obj[11].toString() : null)
                            .bankId(Objects.nonNull(obj[12]) ? obj[12].toString() : null)
                            .email(Objects.nonNull(obj[13]) ? obj[13].toString() : null)
                            .bankName(Objects.nonNull(obj[14]) ? obj[14].toString() : null)
                            .bankAccOwer(Objects.nonNull(obj[15]) ? obj[15].toString() : null)
                            .build())
                    .partyB(PartyRequest.builder()
                            .id(Objects.nonNull(obj[16]) ? obj[16].toString() : null)
                            .name(Objects.nonNull(obj[17]) ? obj[17].toString() : null)
                            .address(Objects.nonNull(obj[18]) ? obj[18].toString() : null)
                            .taxNumber(Objects.nonNull(obj[19]) ? obj[19].toString() : null)
                            .presenter(Objects.nonNull(obj[20]) ? obj[20].toString() : null)
                            .position(Objects.nonNull(obj[21]) ? obj[21].toString() : null)
                            .businessNumber(Objects.nonNull(obj[22]) ? obj[22].toString() : null)
                            .bankId(Objects.nonNull(obj[23]) ? obj[23].toString() : null)
                            .email(Objects.nonNull(obj[24]) ? obj[24].toString() : null)
                            .bankName(Objects.nonNull(obj[25]) ? obj[25].toString() : null)
                            .bankAccOwer(Objects.nonNull(obj[26]) ? obj[26].toString() : null)
                            .build())
                    .file(Objects.nonNull(obj[27]) ? obj[27].toString() : null)
                    .signA(Objects.nonNull(obj[28]) ? obj[28].toString() : null)
                    .signB(Objects.nonNull(obj[29]) ? obj[29].toString() : null)
                    .build();
        }
        return contractRequest;
    }

    @Override
    public BaseResponse delete(String id) throws IOException {
        Contract contract = contractRepository.findById(id).get();
        contract.setMarkDeleted(true);
        contractRepository.save(contract);
        elasticSearchService.deleteDocumentById("contract", id);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, null);
    }

    @Override
    public BaseResponse findContractPartyById(String id) {
        Optional<ContractParty> contractPartyOptional = contractPartyRepository.findByTaxNumber(id);
        ContractParty contractParty = contractPartyOptional.orElse(null);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, contractParty);
    }

    @Override
    public Void sync() {
        List<String> ids = contractRepository.findAll().stream().map(m -> m.getId()).toList();
        ids.forEach(f -> {
            ContractRequest contract = findById(f);
            try {
                if (contract.getId() != null) {
                    elasticSearchService.indexDocument("contract", contract, ContractRequest::getId);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }

    @Override
    public String signContract(SignContractDTO signContractDTO) throws Exception {
        ContractRequest contractRequest = findById(signContractDTO.getContractId());
        Contract contract = contractRepository.findById(signContractDTO.getContractId()).orElse(null);
        Context context = new Context();
        if (contract != null) {
            if (!signContractDTO.isCustomer()) {
                contract.setSignA(signContractDTO.getSignImage());
                context.setVariable("signA", signContractDTO.getSignImage());
                context.setVariable("signB", contract.getSignB());
                if (!StringUtils.isBlank(contract.getSignB())) {
                    contract.setStatus(Constants.STATUS.SUCCESS);
                    contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), signContractDTO.getComment(), Constants.STATUS.SUCCESS);
                } else {
                    contract.setStatus(Constants.STATUS.PROCESSING);
                    contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), signContractDTO.getComment(), Constants.STATUS.SIGN_A);
                }
            } else {
                contract.setSignB(signContractDTO.getSignImage());
                context.setVariable("signB", signContractDTO.getSignImage());
                context.setVariable("signA", contract.getSignA());
                if (!StringUtils.isBlank(contract.getSignA())) {
                    contract.setStatus(Constants.STATUS.SUCCESS);
                    contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), signContractDTO.getComment(), Constants.STATUS.SUCCESS);
                } else {
                    contract.setStatus(Constants.STATUS.PROCESSING);
                    contractHistoryService.createContractHistory(contract.getId(), contract.getName(), contract.getCreatedBy(), signContractDTO.getComment(), Constants.STATUS.SIGN_B);
                }
            }
            context.setVariable("partyA", contractRequest.getPartyA());
            context.setVariable("partyB", contractRequest.getPartyB());
            context.setVariable("info", contract);
            context.setVariable("date", contract.getCreatedDate());
            String html = pdfUtils.templateEngine().process("templates/new_contract.html", context);
            File file = pdfUtils.generatePdf(html, contract.getName() + "_" + UUID.randomUUID());
            contract.setFile(cloudinaryService.uploadPdf(file));
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.warn("Failed to delete the file: {}", file.getAbsolutePath());
                }
            }
            contractRepository.save(contract);
            return "Sign ok";
        } else {
            return "Failed";
        }
    }

    @Override
    public BaseResponse getContractSignById(String id) {
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, findById(id));
    }

}
