package com.fpt.servicecontract.contract.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignContractResponse {
    private String res;
    private String status;
    private boolean canSend = false;
    private boolean canSendForMng = false;
    private boolean canResend = false;
    private boolean sign = false;
}
