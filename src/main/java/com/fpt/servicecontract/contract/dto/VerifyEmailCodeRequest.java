package com.fpt.servicecontract.contract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailCodeRequest {
    private String email;
    private Integer code;
}
