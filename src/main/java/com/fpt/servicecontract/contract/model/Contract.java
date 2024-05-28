package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;

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
    private String term;
    private String file;
    @Column(name="content", columnDefinition="LONGTEXT")
    private String createdBy;
    private Date contractSignDate;
    private Date contractStartDate;
    private Date contractEndDate;
    private Date createdDate;
    private Date updatedDate;
}
