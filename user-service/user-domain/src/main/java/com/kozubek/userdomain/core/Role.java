package com.kozubek.userdomain.core;

import com.kozubek.commondomain.vo.RoleId;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Role {
    private final RoleId id;
    private final String name;

    public Role(String name) {
        this.id = new RoleId(null);
        this.name = name;
    }

    public Role(UUID id, String name) {
        this.id = new RoleId(id);
        this.name = name;
    }
}