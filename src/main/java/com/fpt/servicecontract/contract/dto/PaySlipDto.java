package com.fpt.servicecontract.contract.dto;

import com.fpt.servicecontract.auth.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
public class PaySlipDto {
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
    private UserDto user;
}
