package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class ContractHistoryDto {
    private String id;
    private String contractId;
    private String createdBy; // email
    private String time;
    private String status;
    private String contractName;
}
