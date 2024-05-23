package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.User;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;
    private LocalDateTime contractSignDate;
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
