package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRequest {
    private String name;
    private String phone;
    private String position;
    private String identificationNumber;
    private String dob;
    private boolean gender;
    private String address;
}
