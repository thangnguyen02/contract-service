package com.fpt.servicecontract.contract.dto;

import lombok.*;

import java.util.List;

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
    private boolean canSend;
    private boolean canSendForMng ;
    private boolean canResend ;
    private boolean canSign ;
    private boolean isApproved ;
    private String approvedBy;
    private String statusCurrent;
    private boolean canUpdate ;
    private boolean canDelete ;
    private boolean canUpdateContractRecieve;
    private boolean canApprove;
    private String customer;
    private List<String> contractAppendicesId;
}
