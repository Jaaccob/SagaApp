package com.kozubek.commonapplication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SystemRole {
    USER_ROLE("ROLE_USER"),
    ADMIN_ROLE("ROLE_ADMIN"),
    PAYMENT_MANAGER_ROLE("ROLE_PAYMENT_MANAGER");

    private final String roleName;
}
