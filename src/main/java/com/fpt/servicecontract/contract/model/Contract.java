package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
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
    private String contractName;//required
    private String contractNumber; //required
    private String partyAId; //required
    private String partyBId;//required
    private String description;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String term;
    private String file;
    private String createdBy; // uuid user
    private LocalDateTime contractSignDate;
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
