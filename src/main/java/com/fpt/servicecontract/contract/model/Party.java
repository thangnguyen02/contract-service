package com.fpt.servicecontract.contract.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Party {
    @Id
    @UuidGenerator
    private String id;
    private String name;
    private String address;
    private String presenter;
    private String position;
    private String businessNumber;
    private String bankName;

    @Column(unique = true)
    private String bankId;


    @Column(unique = true)
    private String bankAccOwer;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String taxNumber;
}
