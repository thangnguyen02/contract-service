package com.fpt.servicecontract.contract.dto.request;

import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
public class PaySlipFormulaUpdateRequest {
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
    private String status;
}