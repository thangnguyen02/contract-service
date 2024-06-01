package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
