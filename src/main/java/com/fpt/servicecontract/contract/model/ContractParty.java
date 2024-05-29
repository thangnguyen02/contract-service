package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

    private String identificationNumber;

    private Date dob;

    private boolean gender;


}
