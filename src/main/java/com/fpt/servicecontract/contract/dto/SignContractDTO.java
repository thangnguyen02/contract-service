package com.fpt.servicecontract.contract.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignContractDTO {
    String contractId;
    String signImage;
    String createdBy;
    String comment;
    boolean isCustomer;
}
