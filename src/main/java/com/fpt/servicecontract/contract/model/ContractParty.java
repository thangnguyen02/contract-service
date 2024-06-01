package com.fpt.servicecontract.contract.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
@Getter
@Setter
@Builder
public class ContractParty {
    @Id
    @UuidGenerator
    private String id;

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
