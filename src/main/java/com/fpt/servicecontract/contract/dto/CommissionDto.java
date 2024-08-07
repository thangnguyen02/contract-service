package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommissionDto {
    private Object user;
    private Double commission;
    private long numberContract;
    private long appendicesNumber;
    private Double commissionAmount;
    private Double totalAmount;
}
