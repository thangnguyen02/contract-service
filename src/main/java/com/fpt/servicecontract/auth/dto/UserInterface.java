package com.fpt.servicecontract.auth.dto;

import com.fpt.servicecontract.auth.model.UserStatus;

public interface UserInterface {
    String getEmail();
    String getPhone();
    String getName();
    UserStatus getStatus();
    String getPosition();
    String getAddress();
    String getDepartment();
    String getIdentificationNumber();
    String getId();
    String getPermissions();
    String getAvatar();
    String getDob();
    String getRole();
    boolean getGender();
}
