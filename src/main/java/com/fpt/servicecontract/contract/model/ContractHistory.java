package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class ContractHistory {
    @Id
    @UuidGenerator
    private String id;
    private String contractId;
    private String createdBy; // email
    private LocalDateTime createdDate;
    private String status;
}
