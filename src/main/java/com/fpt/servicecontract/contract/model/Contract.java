package com.fpt.servicecontract.contract.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
public class Contract {
    @Id
    @UuidGenerator
    private String id;
    private String contractName;//required
    private String contractNumber; //required
    private String partyAId; //required
    private String partyBId;//required
    private String description;
    private String term;
    private String file;
    @Column(columnDefinition="LONGTEXT")
    private String content;
    private String signA;
    private String signB;
    private String createdBy;
    private Date contractSignDate;
    private Date contractStartDate;
    private Date contractEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
