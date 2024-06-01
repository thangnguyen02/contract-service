package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractTemplateRequest {
    private String nameContract;//required
    private String numberContract; //required
    private String ruleContract;
    private String termContract;
    private String name;
    private String address;
    private String taxNumber;
    private String presenter;
    private String position;
    private String businessNumber;
    private String bankId;
    private String bankName;
    private String bankAccOwer;
    private String email;
    private String status;
}
