package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "pay_slip_formula")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaySlipFormula {
    @Id
    @UuidGenerator
    private String id;
    private Double fromValueContract;
    private Double toValueContract;
    private Double commissionPercentage;
    private Double baseSalary;
    private Double clientDeploymentPercentage;
    private Double bonusReachesThreshold;
    private Double foodAllowance;
    private Double transportationOrPhoneAllowance;
    private String type;
    private Double bonusAfter1Year;
}
