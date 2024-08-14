package com.fpt.servicecontract.contract.dto;

import com.fpt.servicecontract.contract.dto.request.PartyRequest;
import com.fpt.servicecontract.contract.model.Party;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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
}
