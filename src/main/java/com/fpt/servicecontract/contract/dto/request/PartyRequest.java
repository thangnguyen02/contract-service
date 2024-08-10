package com.fpt.servicecontract.contract.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyRequest {
    private String id;

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

    private String phone;
}
