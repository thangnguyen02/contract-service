package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.contract.dto.OldContractDto;
import com.fpt.servicecontract.contract.model.OldContract;
import com.fpt.servicecontract.contract.repository.OldContractRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.contract.service.OldContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.PdfUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OldContractServiceImpl implements OldContractService {

    private final OldContractRepository oldContractRepository;
    private final CloudinaryService cloudinaryService;
    private final PdfUtils pdfUtils;
    private final JwtService jwtService;

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
                        .contractSignDate(item.getContractSignDate())
                        .contractEndDate(item.getContractEndDate())
                        .contractStartDate(item.getContractStartDate())
                        .build()
        ).toList();

        Page<OldContractDto> pageObject = new PageImpl<>(oldContractDtos, pageable, oldContracts.getTotalElements());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Successfully", true, pageObject);
    }

    @Override
    public BaseResponse create(String token, CreateUpdateOldContract oldContractDto, MultipartFile[] images) {
        OldContract contract = new OldContract();
        String email = jwtService.extractUsername(token);
        contract.setCreatedBy(email);
        contract.setContractName(contract.getContractName());
        contract.setContractEndDate(contract.getContractEndDate());
        contract.setContractStartDate(contract.getContractStartDate());
        contract.setContractSignDate(contract.getContractSignDate());

        try {
            Context context = new Context();
            List<byte[]> imageList = new ArrayList<>();
            for(MultipartFile image : images) {
                byte[] imageBase64 = Base64.encodeBase64(image.getBytes());
                imageList.add(imageBase64);
            }
            context.setVariable("content", imageList);
            context.setVariable("signa",
                    "data:image/png;base64,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");
            String html = pdfUtils.templateEngine().process("templates/test.html", context);
            File file = pdfUtils.generatePdf(html, "file_hihi");

            contract.setFile(cloudinaryService.uploadPdf(file));

        } catch (IOException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Upload Contract Failed", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Cann't create file from images", false, e);
        }

        try {
            oldContractRepository.save(contract);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, OldContractDto.builder()
                    .id(contract.getId())
                    .contractName(contract.getContractName())
                    .createdBy(jwtService.extractUsername(token))
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Create Failed", false, null);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BaseResponse delete(String contractId) {
        var oldContract = oldContractRepository.findById(contractId);
        if (oldContract.isPresent()) {
            OldContract oldCon = oldContract.get();
            oldCon.setIsDeleted(true);
            oldContractRepository.save(oldCon);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Successful", true, oldCon.getId());
        }
        return new BaseResponse(Constants.ResponseCode.FAILURE, "Delete Failed", false, null);
    }
}
