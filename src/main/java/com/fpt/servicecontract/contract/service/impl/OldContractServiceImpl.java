package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.contract.dto.OldContractDto;
import com.fpt.servicecontract.contract.model.OldContract;
import com.fpt.servicecontract.contract.repository.OldContractRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.contract.service.ContractTypeService;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import com.fpt.servicecontract.contract.service.OldContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DateUltil;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OldContractServiceImpl implements OldContractService {

    private final OldContractRepository oldContractRepository;
    private final CloudinaryService cloudinaryService;
    private final PdfUtils pdfUtils;
    private final JwtService jwtService;
    private final ElasticSearchService elasticSearchService;
    private final ContractTypeService contractTypeService;
    @Override
    public BaseResponse getContracts(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<OldContract> oldContracts = oldContractRepository.findAll(pageable);

        if (oldContracts.getTotalElements() == 0) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Not have any contract present", false, null);
        }

        List<OldContractDto> oldContractDtos = oldContracts.get().map(
                item -> OldContractDto.builder()
                        .id(item.getId())
                        .contractName(item.getContractName())
                        .createdBy(item.getCreatedBy())
                        .content(item.getContent())
                        .file(item.getFile())
                        .contractSignDate(String.valueOf(item.getContractSignDate()))
                        .contractEndDate(String.valueOf(item.getContractEndDate()))
                        .contractStartDate(String.valueOf(item.getContractStartDate()))
                        .contractTypeId(String.valueOf(item.getContractTypeId()))
                        .build()
        ).toList();

        Page<OldContractDto> pageObject = new PageImpl<>(oldContractDtos, pageable, oldContracts.getTotalElements());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Successfully", true, pageObject);
    }

    @Override
    public BaseResponse create(String token, CreateUpdateOldContract oldContractDto, MultipartFile[] images) throws Exception {
        OldContract contract = new OldContract();
        String email = jwtService.extractUsername(token.substring(7));
        contract.setCreatedBy(email);
        contract.setContractName(oldContractDto.getContractName());
        contract.setContractEndDate(DateUltil.stringToDate(oldContractDto.getContractEndDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setContractStartDate(DateUltil.stringToDate(oldContractDto.getContractStartDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setContractSignDate(DateUltil.stringToDate(oldContractDto.getContractSignDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setCreatedDate(new Date());
        contract.setContractTypeId(oldContractDto.getContractTypeId());
        if (Objects.requireNonNull(List.of(images).get(0).getOriginalFilename()).endsWith(".pdf")) {
            contract.setFile(cloudinaryService.uploadImage(List.of(images).get(0)));
            contract.setContent(pdfUtils.getTextFromPdfFile(List.of(images).get(0)));
            oldContractRepository.save(contract);
            contract.setContractTypeId(contractTypeService.getContractTypeById(oldContractDto.getContractTypeId()).get().getTitle());
            elasticSearchService.indexDocument("old_contract", contract, OldContract::getId);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, OldContractDto.builder()
                    .id(contract.getId())
                    .contractName(contract.getContractName())
                    .createdBy(email)
                    .file(contract.getFile())
                    .build());
        } else {
            try {
                contract.setContent(oldContractDto.getContent());
                Context context = new Context();
                List<String> imageList = new ArrayList<>();
                for (MultipartFile image : images) {
                    byte[] fileBytes = IOUtils.toByteArray(image.getInputStream());
                    String encodedFile = "data:image/png;base64," + Base64.encodeBase64String(fileBytes);
                    imageList.add(encodedFile);
                }

                context.setVariable("images", imageList);
                String html = pdfUtils.templateEngine().process("templates/old_contract.html", context);
                File file = pdfUtils.generatePdf(html, contract.getContractName() + "_" + UUID.randomUUID());
                contract.setFile(cloudinaryService.uploadPdf(file));
                if (file.exists() && file.isFile()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        log.warn("Failed to delete the file: {}", file.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, "Upload Contract Failed", true, null);
            } catch (Exception e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, "Can't create file from images", false, e);
            }

            try {
                oldContractRepository.save(contract);
                contract.setContractTypeId(contractTypeService.getContractTypeById(oldContractDto.getContractTypeId()).get().getTitle());
                elasticSearchService.indexDocument("old_contract", contract, OldContract::getId);
                return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, OldContractDto.builder()
                        .id(contract.getId())
                        .contractName(contract.getContractName())
                        .createdBy(email)
                        .file(contract.getFile())
                        .build());
            } catch (Exception e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, "Create Failed", false, null);
            }
        }

    }

    @Override
    public BaseResponse createWithMobile(String token, CreateUpdateOldContract oldContractDto, String[] images) throws Exception {
        OldContract contract = new OldContract();
        String email = jwtService.extractUsername(token.substring(7));
        contract.setCreatedBy(email);
        contract.setContractName(oldContractDto.getContractName());
        contract.setContractEndDate(DateUltil.stringToDate(oldContractDto.getContractEndDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setContractStartDate(DateUltil.stringToDate(oldContractDto.getContractStartDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setContractSignDate(DateUltil.stringToDate(oldContractDto.getContractSignDate(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        contract.setCreatedDate(new Date());
        try {
            contract.setContent(oldContractDto.getContent());
            Context context = new Context();
            List<String> imageList = new ArrayList<>();
            for (String img : images) {
                String encodedFile = "data:image/png;base64," + img.trim();
                imageList.add(encodedFile);
            }
            context.setVariable("images", imageList);
            String html = pdfUtils.templateEngine().process("templates/old_contract.html", context);
            File file = pdfUtils.generatePdf(html, contract.getContractName() + "_" + UUID.randomUUID());
            contract.setFile(cloudinaryService.uploadPdf(file));
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.warn("Failed to delete the file: {}", file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Upload Contract Failed", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Can't create file from images", false, e);
        }

        try {
            oldContractRepository.save(contract);
            elasticSearchService.indexDocument("old_contract", contract, OldContract::getId);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, OldContractDto.builder()
                    .id(contract.getId())
                    .contractName(contract.getContractName())
                    .createdBy(email)
                    .file(contract.getFile())
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Create Failed", false, null);
        }

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BaseResponse delete(String contractId) throws IOException {
        var oldContract = oldContractRepository.findById(contractId);
        if (oldContract.isPresent()) {
            OldContract oldCon = oldContract.get();
            oldCon.setIsDeleted(true);
            oldContractRepository.save(oldCon);
            elasticSearchService.deleteDocumentById("old_contract", contractId);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Successful", true, oldCon.getId());
        }
        return new BaseResponse(Constants.ResponseCode.FAILURE, "Delete Failed", false, null);
    }

    @Override
    public Void sync() {
        List<OldContract> oldContracts = oldContractRepository.findAll();
        if (!CollectionUtils.isEmpty(oldContracts)) {
            oldContracts.forEach(o -> {
                try {
                    elasticSearchService.indexDocument("old_contract", o, OldContract::getId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return null;
    }
}
