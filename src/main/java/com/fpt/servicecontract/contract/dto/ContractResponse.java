package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractResponse {
    private String name;
    private String createdBy;
    private String file;
    private String createdDate;
}
