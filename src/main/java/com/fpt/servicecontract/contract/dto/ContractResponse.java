package com.fpt.servicecontract.contract.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private String id;
    private String name;
    private String createdBy;
    private String file;
    private String createdDate;
    private String status;
    private boolean isUrgent;
}
