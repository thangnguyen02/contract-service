package com.fpt.servicecontract.contract.dto;

import lombok.Data;

@Data
public class SmsRequest {

    private String to;
    private String message;
}
