package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Contract {

    @Id
    @UuidGenerator
    private String id;
    private String contractNumber;
    LocalDateTime contractDate;
    LocalDateTime contractStartDate;
    LocalDateTime contractStartEnd;
    private String contractTypeId;
    private String statusId;
    private String partyAId;
    private String partyBId;
    private String createdBy;
    private String description;

}
