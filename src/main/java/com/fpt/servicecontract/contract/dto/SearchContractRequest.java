package com.fpt.servicecontract.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchContractRequest {
    private String contractName;
    private String contractNumber;
    LocalDateTime contractDate;
    LocalDateTime contractStartDate;
    LocalDateTime contractEndDate;
    private String contractTypeId;
    private String statusId;
    private String partyAName;
    private String partyBName;
    private String createdBy;
    private String description;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int page;
    private int size;
}
