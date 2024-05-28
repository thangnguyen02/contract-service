package com.fpt.servicecontract.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateOldContract {
    private String contractName;
    private String content;
    private String contractSignDate;
    private String contractStartDate;
    private String contractEndDate;
}
