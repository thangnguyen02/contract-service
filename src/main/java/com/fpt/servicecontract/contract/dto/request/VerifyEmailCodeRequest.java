package com.fpt.servicecontract.contract.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailCodeRequest {
    private String email;
    private String phone;
    private Integer code;
}
