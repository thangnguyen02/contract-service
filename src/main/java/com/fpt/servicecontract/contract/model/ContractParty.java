package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
@Getter
@Setter
public class ContractParty {
    @Id
    @UuidGenerator
    private String id;

    private String name;

    private String phone;

    private String email;

    private String position;

    private String identificationNumber;

    private Date dob;

    private boolean gender;

    private String address;
}
