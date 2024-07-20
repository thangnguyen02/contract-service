package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "pay_slip")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaySlip {
    @Id
    @UuidGenerator
    private String id;
    private Double fromValueContract;
    private Double toValueContract;
    private Double commissionPercentage;
    private Double deployCustomerPercentage;

}
