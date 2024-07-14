package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationContractNumberDto {
    private Integer approvedCount;
    private Integer waitApprovedCount;
    private Integer waitSignACount;
    private Integer successCount;
    private Integer signedCount;
    private Integer waitSignBCount;
}
