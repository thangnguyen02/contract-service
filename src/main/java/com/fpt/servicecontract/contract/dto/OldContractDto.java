package com.fpt.servicecontract.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OldContractDto {
    private String id;
    private String contractName;
    private String createdBy;
    private String content;
    private String file;
    private LocalDateTime contractSignDate;
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
}
