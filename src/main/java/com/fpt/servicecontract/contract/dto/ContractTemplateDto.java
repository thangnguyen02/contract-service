package com.fpt.servicecontract.contract.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Builder
@Getter
@Setter
public class ContractTemplateDto {
    private String id;
    private String nameContract;//required
    private String numberContract; //required
    private String ruleContract;
    private String termContract;
    private String name;
    private String address;
    private String taxNumber;
    private String presenter;
    private String position;
    private String businessNumber;
    private String bankId;
    private String bankName;
    private String bankAccOwer;
    private String email;
    private String createdDate;
    private String updatedDate;
}
