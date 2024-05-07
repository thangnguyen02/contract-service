package com.fpt.servicecontract.auth.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum Permission {

    CREATE_CONTRACT("PERMISSION_CREATE_CONTRACT"),
    READ_CONTRACT("PERMISSION_READ_CONTRACT"),
    UPDATE_CONTRACT("PERMISSION_UPDATE_CONTRACT"),
    DELETE_CONTRACT("PERMISSION_DELETE_CONTRACT"),
    MANAGE_CONTRACT("PERMISSION_MANAGE_CONTRACT"),
    CREATE_USER("PERMISSION_CREATE_USER"),
    READ_USER("PERMISSION_READ_USER"),
    UPDATE_USER("PERMISSION_UPDATE_USER"),
    DELETE_USER("PERMISSION_DELETE_USER"),
    MANAGE_USER("PERMISSION_MANAGE_USER"),
    CREATE_ROLE("PERMISSION_CREATE_ROLE"),
    READ_ROLE("PERMISSION_READ_ROLE"),
    UPDATE_ROLE("PERMISSION_UPDATE_ROLE"),
    DELETE_ROLE("PERMISSION_DELETE_ROLE"),
    MANAGE_ROLE("PERMISSION_MANAGE_ROLE");

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
