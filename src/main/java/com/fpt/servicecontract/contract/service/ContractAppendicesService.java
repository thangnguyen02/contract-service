package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.request.ContractRequest;
import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ContractAppendicesService {
    BaseResponse getAll(Pageable p, String email, String status, String contractId);

    BaseResponse getById(String id);

    BaseResponse save(ContractAppendices contractAppendices, String email);

    BaseResponse deleteById(String id);

    BaseResponse update(String id, ContractAppendices contractAppendices);

    BaseResponse sendMail(String bearerToken, String[] to, String[] cc, String subject, String htmlContent, MultipartFile[] attachments, String contractId, String status, String description);

    BaseResponse getByContractId(String contractId, int page, int size);

    BaseResponse publicSendMail(String[] to,String[] cc,String subject,String htmlContent,String createdBy,String contractAppendicesId,String status,String description);

    BaseResponse getContractSignById(String id);


}
