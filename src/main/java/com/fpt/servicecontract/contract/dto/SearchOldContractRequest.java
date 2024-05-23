package com.fpt.servicecontract.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchOldContractRequest {
    private Integer page;
    private Integer size;
}
