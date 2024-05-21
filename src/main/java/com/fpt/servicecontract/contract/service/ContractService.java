package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractDto;
import com.fpt.servicecontract.contract.dto.SearchContractRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ContractService {
    public String createOldContract(String content ,List<MultipartFile> images) ;
    public Page<ContractDto> searchContract(SearchContractRequest searchContractRequest);
    public ContractDto create(ContractDto contractDto);
    public ContractDto update(ContractDto contractDto);
    public String delete(String id);

}
