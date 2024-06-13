package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StatisticalDto {
    private Integer resultBySignStatus;
    private Integer resultByMonth;
}
