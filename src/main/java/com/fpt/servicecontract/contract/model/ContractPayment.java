package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
@Entity
@Table
@Getter
@Setter
public class ContractPayment {
    @Id
    @UuidGenerator
    private String id;
}
