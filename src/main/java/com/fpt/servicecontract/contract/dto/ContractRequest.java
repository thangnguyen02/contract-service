package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequest {
    private String contractName;
    private String contractNumber;
    private String rule;
    private String term;
    private PartyRequest partyA;
    private PartyRequest partyB;
}

