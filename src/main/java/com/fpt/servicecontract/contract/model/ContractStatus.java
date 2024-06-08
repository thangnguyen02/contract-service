package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class ContractStatus {
    @Id
    @UuidGenerator
    private String id;
    private String sender;//id email
    private String receiver;// id email
    private String contractId;
    private LocalDateTime sendDate;

}
