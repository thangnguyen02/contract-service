package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequest {
    private String contractName;
    private String contractNumber;
    private String description;
    private String term;
    private String content;
    private PartyRequest partyA;
    private PartyRequest partyB;
}

