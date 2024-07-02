package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class ContractAppendices {
    @Id
    @UuidGenerator
    private String id;
    private String contract_id;
    private String appendix_number;
    private String name;
    private String number;
    @Column(columnDefinition="LONGTEXT")
    private String rule;
    @Column(columnDefinition="LONGTEXT")
    private String term;
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
    private String approvedBy;
}
