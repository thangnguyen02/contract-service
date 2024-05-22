package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.ContractDto;
import com.fpt.servicecontract.contract.dto.ContractInterface;
import com.fpt.servicecontract.contract.dto.SearchContractRequest;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.QueryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {


    private final ContractRepository contractRepository;

    @Override
    public String createOldContract(String content, List<MultipartFile> images) {
        return "ok";
    }

    @Override
    public Page<ContractDto> searchContract(SearchContractRequest searchContractRequest) {
        Pageable pageable = Pageable.ofSize(searchContractRequest.getSize()).withPage(searchContractRequest.getPage());

        var contracts = contractRepository.search(
                QueryUtils.appendPercent(searchContractRequest.getContractName()),
                QueryUtils.appendPercent(searchContractRequest.getContractNumber()),
                QueryUtils.appendPercent(searchContractRequest.getContractTypeId()),
                QueryUtils.appendPercent(searchContractRequest.getStatusId()),
                QueryUtils.appendPercent(searchContractRequest.getCreatedBy()),
                QueryUtils.appendPercent(searchContractRequest.getContent()),
                QueryUtils.appendPercent(searchContractRequest.getPartyAName()),
                QueryUtils.appendPercent(searchContractRequest.getPartyBName()),
                pageable
        );

        List<ContractDto> contractDtos = contracts.toList().stream().map(item -> ContractDto.builder()
                .content(item.getContent())
                .contractName(item.getContractName())
                .build()).toList();

        Page<ContractDto> page = new PageImpl<>(contractDtos, pageable, contracts.getTotalElements());
        return page;
    }

    @Override
    public ContractDto create(ContractDto contractDto) {
        return null;
    }

    @Override
    public ContractDto update(ContractDto contractDto) {
        return null;
    }

    @Override
    public String delete(String id) {
        return "";
    }
}
