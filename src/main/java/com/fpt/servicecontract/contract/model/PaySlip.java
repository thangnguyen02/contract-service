package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

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
    private String email;
    private Double commissionPercentage;
    private Double totalValueContract;
    private Double baseSalary;
    private Double clientDeploymentPercentage;
    private Double bonusReachesThreshold;
    private Double foodAllowance;
    private Double transportationOrPhoneAllowance;
    private Double totalSalary;
    private LocalDate createdDate;
    private String type;
    private Integer numberSaleManage;
}
