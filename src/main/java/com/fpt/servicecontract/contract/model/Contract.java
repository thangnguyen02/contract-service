package com.fpt.servicecontract.contract.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @UuidGenerator
    private String id;
    private String name;
    private String number;
    @Column(columnDefinition="LONGTEXT")
    private String rule;
    @Column(columnDefinition="LONGTEXT")
    private String term;
    private String partyAId;
    private String partyBId;
    private String file;
    @Column(columnDefinition="LONGTEXT")
    private String signA;
    @Column(columnDefinition="LONGTEXT")
    private String signB;
    private String createdBy;
    private Date contractSignDate;
    private Date contractStartDate;
    private Date contractEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean markDeleted;
    private String status;
    private boolean isUrgent;
}
