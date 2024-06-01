package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
@Getter
@Setter
@Builder
public class ContractTemplate {
    @Id
    @UuidGenerator
    private String id;

    private String nameContract;//required
    private String numberContract; //required

    @Column(columnDefinition="LONGTEXT")
    private String ruleContract;
    @Column(columnDefinition="LONGTEXT")
    private String termContract;

    // FE fill to partyA
    private String name;

    private String address;

    private String taxNumber;

    private String presenter;

    private String position;

    private String businessNumber;

    private String bankId;

    private String bankName;

    private  String bankAccOwer;

    private String email;
}
