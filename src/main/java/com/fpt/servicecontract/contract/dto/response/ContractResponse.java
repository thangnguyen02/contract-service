package com.fpt.servicecontract.contract.dto.response;

import com.fpt.servicecontract.auth.dto.UserDto;
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
    private boolean canApprove;
    private String customer;
    private List<String> contractAppendicesId;
    private boolean canSendForCustomer;
    private Double value;
    private String rejectedBy;
    private boolean canRejectSign;
    private boolean isDraft;
    private String numberContract;
    private UserDto user;
}
