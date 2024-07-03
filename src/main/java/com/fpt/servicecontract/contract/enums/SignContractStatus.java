package com.fpt.servicecontract.contract.enums;

public enum SignContractStatus {
    NEW("NEW"),
    WAIT_APPROVE("WAIT_APPROVE"),
    APPROVE_FAIL("APPROVE_FAIL"),
    WAIT_SIGN_A("WAIT_SIGN_A"),
    SIGN_A_FAIL("SIGN_A_FAIL"),
    WAIT_SIGN_B("WAIT_SIGN_B"),
    SIGN_B_FAIL("SIGN_B_FAIL"),
    SUCCESS("SUCCESS"),
    DONE("DONE"),
    APPROVED("APPROVED"),
    SIGN_A_OK("SIGN_A_OK"),
    SIGN_B_OK("SIGN_B_OK"),
    MANAGER_CONTRACT("MANAGER_CONTRACT"),
    ALL("ALL")
    ;

    private final String signContractStatus;

    SignContractStatus(String userStatus) {
        this.signContractStatus = userStatus;
    }

}
