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
public class CreateUpdateOldContract {
    private String content;
    private LocalDateTime contractSignDate;
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
}
