package com.kozubek.userdomain.port;

import com.kozubek.userdomain.core.Role;

import java.util.Set;

public interface RoleRepository {
    Set<Role> findAll();
}
