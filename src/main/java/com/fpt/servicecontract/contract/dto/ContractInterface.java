package com.fpt.servicecontract.contract.dto;

import java.time.LocalDateTime;

public interface ContractInterface {
     String  getContractName();
     String getContractNumber();
     LocalDateTime getContractDate();
     LocalDateTime getContractStartDate();
     LocalDateTime getContractEndDate();
     String getContractTypeId();
     String getStatusId();
     String getPartyAName();
     String getPartyBName();
     String getCreatedBy();
     String getDescription();
     String getContent();
     LocalDateTime getCreatedDate();
     LocalDateTime getUpdatedDate();
}
