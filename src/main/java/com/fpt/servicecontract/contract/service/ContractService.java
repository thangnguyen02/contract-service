package com.fpt.servicecontract.contract.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.servicecontract.contract.dto.request.ContractRequest;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.contract.dto.response.SignContractResponse;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest, String email);

    BaseResponse findAll(Pageable p, String email, String status, String search);

    ContractRequest findById(String id);

    BaseResponse delete(String id) throws IOException;

    BaseResponse findContractPartyById(String id);

    Void sync();

    BaseResponse signContract(SignContractDTO signContractDTO);

    BaseResponse getContractSignById(String id);

    BaseResponse sendMail(String bearerToken,String[] to,String[] cc,String subject,String htmlContent, MultipartFile[] attachments, String contractId, String status, String description);

    BaseResponse getNotificationContractNumber(String email);

    BaseResponse publicSendMail(String[] to,String[] cc,String subject,String htmlContent,String createdBy,String contractId,String status,String description);
}
