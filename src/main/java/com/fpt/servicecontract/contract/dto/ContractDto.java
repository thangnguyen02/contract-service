package com.fpt.servicecontract.contract.dto;

import com.fpt.servicecontract.contract.model.ContractParty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {

    private String id;
    private String contractName;
    private String contractNumber;
    private LocalDateTime contractDate;
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
    private String contractTypeId;
    private String statusId;
    private String statusName;
    private ContractParty partyA;
    private ContractParty partyB;
    private String createdBy;
    private String description;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
