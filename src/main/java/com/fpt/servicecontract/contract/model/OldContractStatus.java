package com.fpt.servicecontract.contract.model;

import lombok.Getter;

@Getter
public enum OldContractStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PROCESSING("PROCESSING");


    private final String oldContractStatus;

    OldContractStatus(String userStatus) {
        this.oldContractStatus = userStatus;
    }
}
