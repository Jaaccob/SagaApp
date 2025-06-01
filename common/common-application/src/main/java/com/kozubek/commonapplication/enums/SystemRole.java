package com.kozubek.commonapplication.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SystemRole {
    USER_ROLE("USER_ROLE"),
    ADMIN_ROLE("ADMIN_ROLE"),
    PAYMENT_MANAGER_ROLE("PAYMENT_MANAGER_ROLE");

    private final String roleName;
}
