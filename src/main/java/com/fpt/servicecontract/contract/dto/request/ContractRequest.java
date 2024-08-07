package com.fpt.servicecontract.contract.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String id;
    private String name;
    private String number;
    private String rule;
    private String term;
    private PartyRequest partyA;
    private PartyRequest partyB;
    private String file;
    private boolean isUrgent;
    private String signA;
    private String signB;
    private String createdBy;
    private String approvedBy;
    private String contractTypeId;
    private Double value;
    private String status;
}

