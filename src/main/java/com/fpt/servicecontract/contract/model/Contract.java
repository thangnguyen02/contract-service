package com.fpt.servicecontract.contract.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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

    private String name;//required
    private String number; //required

    @Column(columnDefinition="LONGTEXT")
    private String rule;
    @Column(columnDefinition="LONGTEXT")
    private String term;

    private String partyAId; //required
    private String partyBId;//required

    private String file;

    private String signA;
    private String signB;
    private String createdBy;
    private Date contractSignDate;
    private Date contractStartDate;
    private Date contractEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
