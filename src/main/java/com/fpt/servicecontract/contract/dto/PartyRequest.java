package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRequest {
    private String name;

    private String address;

    private String taxNumber;

    private String presenter;

    private String position;

    private String businessNumber;

    private String bankId;

    private String bankName;

    private  String bankAccOwer;

    private String email;
}