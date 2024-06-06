package com.fpt.servicecontract.contract.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum EntityType {
    CONTRACT("contract"),
    USER("users"),
    ;

    private final String entityType;

    EntityType(String userStatus) {
        this.entityType = userStatus;
    }
}
