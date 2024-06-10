package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ContractService {

    BaseResponse createContract(ContractRequest contractRequest, String email) throws Exception;

    BaseResponse findAll(Pageable p, String email);

    ContractRequest findById(String id);

    BaseResponse delete(String id) throws IOException;

    BaseResponse findContractPartyById(String id);

    Void sync();

    String signContract(SignContractDTO signContractDTO) throws Exception;

}
