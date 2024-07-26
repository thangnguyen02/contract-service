package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractType {
    @Id
    @UuidGenerator
    private String id;

    private String title;

    private String description;

    private Boolean markDeleted;

    @Column(name = "created_date")
    private LocalDate createdDate;
}
