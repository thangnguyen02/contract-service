package com.fpt.servicecontract.auth.dto;

import com.fpt.servicecontract.auth.model.UserStatus;

public interface UserInterface {
    String getEmail();
    String getPhone();
    String getName();
    UserStatus getStatus();
    String getPosition();
    String getDepartment();
    String getIdentificationNumber();
    String getPermissions();
}
