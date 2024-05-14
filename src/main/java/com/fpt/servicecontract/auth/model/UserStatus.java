package com.fpt.servicecontract.auth.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PROCESSING("PROCESSING");


    private final String userStatus;

    UserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
