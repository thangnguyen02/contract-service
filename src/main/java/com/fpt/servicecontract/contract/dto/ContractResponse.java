package com.fpt.servicecontract.contract.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private String id;
    private String name;
    private String createdBy;
    private String file;
    private String createdDate;
    private String status;
    private boolean isUrgent;
    private boolean canSend = true;
    private boolean canSendForMng = false;
    private boolean canResend = false;
    private boolean sign = false;
    private boolean isApproved = false;
    private String approvedBy;
    private String statusCurrent;
    private boolean canUpdate = true;
    private boolean canDelete = true;
}
