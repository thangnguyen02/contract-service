package com.fpt.servicecontract.contract.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.contract.dto.SignContractResponse;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest, String email) throws Exception;

    BaseResponse findAll(Pageable p, String email, String status, String search) throws JsonProcessingException;

    ContractRequest findById(String id);

    BaseResponse delete(String id) throws IOException;

    BaseResponse findContractPartyById(String id);

    Void sync();

    String signContract(SignContractDTO signContractDTO) throws Exception;

    BaseResponse getContractSignById(String id);

    SignContractResponse sendMail(String bearerToken,String[] to,String[] cc,String subject,String htmlContent, MultipartFile[] attachments, String contractId, String status, String description);

    BaseResponse getNotificationContractNumber(String email);
}
