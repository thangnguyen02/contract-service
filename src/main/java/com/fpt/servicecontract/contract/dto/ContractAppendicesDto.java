package com.fpt.servicecontract.contract.dto;

import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.contract.dto.request.PartyRequest;
import com.fpt.servicecontract.contract.model.Party;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractAppendicesDto {
    private String id;
    private String contractId;
    private String name;
    private String number;
    private String rule;
    private String term;
    private String file;
    private String signA;
    private String signB;
    private String createdBy;
    private Date contractSignDate;
    private Date contractAppendicesStartDate;
    private Date contractAppendicesEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String status;
    private String approvedBy;
    private boolean markDeleted;
    private Double value;
    private PartyRequest partyA;
    private PartyRequest partyB;
    private String currentStatus;
    private boolean canSend;
    private boolean canSendForMng ;
    private boolean canResend ;
    private boolean canSign ;
    private boolean isApproved ;
    private String statusCurrent;
    private boolean canUpdate ;
    private boolean canDelete ;
    private boolean canApprove;
    private String customer;
    private boolean canSendForCustomer;
    private String rejectedBy;
    private boolean canRejectSign;
    private boolean isDraft;
    private String numberContract;
    private UserDto user;
}
