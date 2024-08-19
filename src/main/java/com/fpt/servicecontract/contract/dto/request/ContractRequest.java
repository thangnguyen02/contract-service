package com.fpt.servicecontract.contract.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String id;
    private String name;
    private String number;
    private String rule;
    private String term;
    private PartyRequest partyA;
    private PartyRequest partyB;
    private String file;
    private boolean isUrgent;
    private String signA;
    private String signB;
    private String createdBy;
    private String approvedBy;
    private String contractTypeId;
    private Double value;
    private String status;
    private String reason;
    private String statusCurrent;
    private boolean canSend;
    private boolean canSendForMng ;
    private boolean canResend ;
    private boolean canSign ;
    private boolean isApproved ;
    private boolean canUpdate ;
    private boolean canDelete ;
    private boolean canApprove;
    private String customer;
    private boolean canSendForCustomer;
    private String rejectedBy;
    private boolean canRejectSign;
    private boolean isDraft;
}

