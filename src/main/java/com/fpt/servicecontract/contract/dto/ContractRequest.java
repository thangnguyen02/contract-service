package com.fpt.servicecontract.contract.dto;

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
}

