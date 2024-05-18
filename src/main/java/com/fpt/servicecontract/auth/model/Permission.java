package com.fpt.servicecontract.auth.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Permission {

    SALE("PERMISSION_SALE"),
    MANAGER("PERMISSION_MANAGER"),
    OFFICE_ADMIN("PERMISSION_OFFICE_ADMIN"),
    OFFICE_STAFF("PERMISSION_OFFICE_STAFF");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public static Set<Permission> getAllPermissions() {
        Set<Permission> permissions = new HashSet<>();
        Collections.addAll(permissions, values());
        return permissions;
    }
}
